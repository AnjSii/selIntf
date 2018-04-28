package com.tomowork.shop.selIntf.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.api.AreaVO;
import com.tomowork.shop.api.ImageVO;
import com.tomowork.shop.assets.tools.ImageType;
import com.tomowork.shop.core.tools.AccessoryUtil;
import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.foundation.domain.Accessory;
import com.tomowork.shop.foundation.domain.Area;
import com.tomowork.shop.foundation.domain.CreateStoreApplication;
import com.tomowork.shop.foundation.domain.Role;
import com.tomowork.shop.foundation.domain.Store;
import com.tomowork.shop.foundation.domain.StoreClass;
import com.tomowork.shop.foundation.domain.StoreGrade;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.AccessoryService;
import com.tomowork.shop.foundation.service.AreaService;
import com.tomowork.shop.foundation.service.CreateStoreApplicationService;
import com.tomowork.shop.foundation.service.RoleService;
import com.tomowork.shop.foundation.service.StoreClassService;
import com.tomowork.shop.foundation.service.StoreGradeService;
import com.tomowork.shop.foundation.service.StoreService;
import com.tomowork.shop.foundation.service.SysConfigService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.entity.StoreApplicationVO;
import com.tomowork.shop.selIntf.entity.StoreApplicationVO.StoreApplicationStatus;
import com.tomowork.shop.selIntf.entity.StoreClassVO;
import com.tomowork.shop.selIntf.entity.StoreGradeVO;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.StoreApplicationService;
import com.tomowork.shop.velocity.tools.view.URIFactory;

@Service
@Transactional
public class StoreApplicationServiceImpl implements StoreApplicationService {

	@Autowired
	private StoreService storeService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private StoreGradeService storeGradeService;

	@Autowired
	private StoreClassService storeClassService;

	@Autowired
	private UserService userService;

	@Autowired
	private CreateStoreApplicationService createStoreApplicationService;

	@Autowired
	private SysConfigService configService;

	@Autowired
	private AccessoryService accessoryService;

	@Autowired
	private RoleService roleService;

	@Autowired
	@Named("imageUriFactory")
	URIFactory uriFactory;

	@Override
	public Long createStoreApplication(StoreApplicationVO storeApplicationVO, String userName) throws NullPointerException,
			IllegalArgumentException, ViolationException {

		if (storeApplicationVO == null || storeApplicationVO.getArea() == null || storeApplicationVO.getArea().getId() == null
				|| storeApplicationVO.getGrade() == null || storeApplicationVO.getGrade().getId() == null || userName == null
				|| storeApplicationVO.getStoreClass() == null || storeApplicationVO.getStoreClass().getId() == null) {
			throw new NullPointerException();
		}

		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		Store store = this.storeService.getObjByProperty("store_name", storeApplicationVO.getStore_name());
		if (store != null) {
			throw new ViolationException("店铺名已经存在");
		}

		Area area = this.areaService.getObjById(storeApplicationVO.getArea().getId());
		if (area == null) {
			throw new ViolationException("区域找不到");
		}
		StoreGrade grade = this.storeGradeService.getObjById(storeApplicationVO.getGrade().getId());
		if (grade == null) {
			throw new ViolationException("无效店铺等级");
		}
		StoreClass storeClass = this.storeClassService.getObjById(storeApplicationVO.getStoreClass().getId());
		if (storeClass == null) {
			throw new ViolationException("无效店铺分类");
		}

		User user = this.userService.getUserByUsername(userName);
		Store user_store = this.storeService.getObjByProperty("user.id", user.getId());
		int store_status = (user_store == null) ? 0 : user_store.getStore_status();
		CreateStoreApplication csa = new CreateStoreApplication();
		switch (store_status) {
			case 0:
				csa.setArea(area);
				csa.setGrade(grade);
				csa.setSc(storeClass);
				csa.setStore_address(storeApplicationVO.getStore_address());
				csa.setStore_name(storeApplicationVO.getStore_name());
				csa.setStore_ower(storeApplicationVO.getStore_ower());
				csa.setStore_ower_card(storeApplicationVO.getStore_ower_card());
				csa.setStore_qq(storeApplicationVO.getStore_qq());
				csa.setStore_telephone(storeApplicationVO.getStore_telephone());
				csa.setStore_zip(storeApplicationVO.getStore_zip());
				csa.setAddTime(new Date());
				csa.setUser(user);
				this.createStoreApplicationService.save(csa);

				Map<String, Object> params = new HashMap<>();
				params.put("type", "SELLER");
				List<Role> roles = this.roleService.query(
						"select obj from Role obj where obj.type=:type",
						params, -1, -1);
				user.getRoles().addAll(roles);
				this.userService.update(user);
				break;
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				throw new ViolationException("您已经开通店铺");
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
		return csa.getId();
	}

	@Override
	public Long imagesAuthenticate(String userName, MultipartFile card_front,
			MultipartFile card_side, MultipartFile card_hand, MultipartFile licebse_file) {
		if (userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}
		Map<String, MultipartFile> fileMap = new HashMap<>();
		fileMap.put("card_front", card_front);
		fileMap.put("card_hand", card_hand);
		fileMap.put("card_side", card_side);
		fileMap.put("licebse_file", licebse_file);
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			if (entry.getValue() != null) {
				String imageType = entry.getValue().getOriginalFilename().substring(entry.getValue().getOriginalFilename()
						.lastIndexOf(".") + 1).toLowerCase();
				ImageType shopImageType = ImageType.typeOf(imageType);
				if (shopImageType == null) {
					throw new ViolationException("图片格式不正确");
				}
			}
		}

		User user = this.userService.getUserByUsername(userName);
		Map<String, Object> params = new HashMap<>();
		params.put("user", user);
		List<CreateStoreApplication> createStoreApplications = this.createStoreApplicationService.
				query("select obj from CreateStoreApplication obj where obj.user=:user order by obj.addTime desc",
						params, -1, -1);
		CreateStoreApplication old_csa;
		Accessory old_card_front = null;
		Accessory old_card_side = null;
		Accessory old_card_hand = null;
		Accessory old_store_license = null;
		if (createStoreApplications.size() == 0) {
			throw new ViolationException("没有开店申请记录");
		} else if (createStoreApplications.size() >= 2) {
			old_csa = createStoreApplications.get(1);
			old_card_front = old_csa.getCard();
			old_card_side = old_csa.getCard_side();
			old_card_hand = old_csa.getCard_hand();
			old_store_license = old_csa.getStore_license();
		}
		CreateStoreApplication csa = createStoreApplications.get(0);
		String path = AccessoryUtil.getUploadPath(this.configService.getSysConfig().getUploadFilePath(), null, "csa")
				+ File.separator + csa.getId();
		CommUtil.createFolder(path);

		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			if (entry.getValue() != null) {
				try (InputStream is = entry.getValue().getInputStream()) {
					String[] type = {entry.getValue().getOriginalFilename().substring(entry.getValue().getOriginalFilename()
							.lastIndexOf(".") + 1).toLowerCase().trim()};
					Map<String, Object> map = CommUtil.saveFileToServer(path, entry.getKey(), is, type);

					Accessory accessory = new Accessory();
					accessory.setName(CommUtil.null2String(map.get("fileName")));
					accessory.setExt(CommUtil.null2String(map.get("mime")));
					accessory.setSize(CommUtil.null2Float(map.get("fileSize")));
					accessory.setPath("csa" + "/" + csa.getId());
					accessory.setWidth(CommUtil.null2Int(map.get("width")));
					accessory.setHeight(CommUtil.null2Int(map.get("height")));
					accessory.setAddTime(new Date());
					this.accessoryService.save(accessory);

					if (entry.getKey().equals("card_front")) {
						csa.setCard(accessory);
						if (old_card_front != null) {
							old_card_front.setDeleteStatus(true);
							this.accessoryService.update(old_card_front);
						}
					} else if (entry.getKey().equals("card_side")) {
						csa.setCard_side(accessory);
						if (old_card_side != null) {
							old_card_side.setDeleteStatus(true);
							this.accessoryService.update(old_card_side);
						}
					} else if (entry.getKey().equals("card_hand")) {
						csa.setCard_hand(accessory);
						if (old_card_hand != null) {
							old_card_hand.setDeleteStatus(true);
							this.accessoryService.update(old_card_hand);
						}
					} else if (entry.getKey().equals("licebse_file")) {
						csa.setStore_license(accessory);
						if (old_store_license != null) {
							old_store_license.setDeleteStatus(true);
							this.accessoryService.update(old_store_license);
						}
					}
				} catch (IOException e) {
					throw new ViolationException("图片格式不正确");
				}
				this.createStoreApplicationService.update(csa);
			}
		}
		return csa.getId();
	}

	@Override
	public StoreApplicationVO getStoreApplication(String userName) {
		if (userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		Map<String, Object> params = new HashMap<>();
		params.put("user", user);
		List<CreateStoreApplication> createStoreApplications = this.createStoreApplicationService.
				query("select obj from CreateStoreApplication obj where obj.user=:user order by obj.addTime desc",
						params, -1, -1);
		if (createStoreApplications.size() == 0) {
			throw new ViolationException("没有申请记录");
		}
		CreateStoreApplication csa = createStoreApplications.get(0);
		StoreApplicationVO storeApplicationVO = new StoreApplicationVO();
		storeApplicationVO.setId(csa.getId());
		storeApplicationVO.setStore_address(csa.getStore_address());
		storeApplicationVO.setStore_name(csa.getStore_name());
		storeApplicationVO.setStore_ower(csa.getStore_ower());
		storeApplicationVO.setStore_ower_card(csa.getStore_ower_card());
		storeApplicationVO.setStore_qq(csa.getStore_qq());
		storeApplicationVO.setStore_telephone(csa.getStore_telephone());
		storeApplicationVO.setStore_zip(csa.getStore_zip());
		if (csa.getAuditLog() != null) {
			storeApplicationVO.setAudit_reason(csa.getAuditLog().getAudit_reason());
		}

		ImageVO cart_front = new ImageVO();
		if (csa.getCard() != null) {
			URI URI = uriFactory.createURI(csa.getCard().getUrl(), null, null);
			cart_front.setUrl(CommUtil.null2String(URI));
		} else {
			URI URI = uriFactory.createURI(this.configService.getSysConfig().getStoreImage().getUrl(), null, null);
			cart_front.setUrl(CommUtil.null2String(URI));
		}
		storeApplicationVO.setCard_front(cart_front);

		ImageVO cart_side = new ImageVO();
		if (csa.getCard_side() != null) {
			URI URI = uriFactory.createURI(csa.getCard_side().getUrl(), null, null);
			cart_side.setUrl(CommUtil.null2String(URI));
		} else {
			URI URI = uriFactory.createURI(this.configService.getSysConfig().getStoreImage().getUrl(), null, null);
			cart_side.setUrl(CommUtil.null2String(URI));
		}
		storeApplicationVO.setCard_side(cart_side);

		ImageVO cart_hand = new ImageVO();
		if (csa.getCard_hand() != null) {
			URI URI = uriFactory.createURI(csa.getCard_hand().getUrl(), null, null);
			cart_hand.setUrl(CommUtil.null2String(URI));
		} else {
			URI URI = uriFactory.createURI(this.configService.getSysConfig().getStoreImage().getUrl(), null, null);
			cart_hand.setUrl(CommUtil.null2String(URI));
		}
		storeApplicationVO.setCard_hand(cart_hand);

		ImageVO licebse_file = new ImageVO();
		if (csa.getStore_license() != null) {
			URI URI = uriFactory.createURI(csa.getStore_license().getUrl(), null, null);
			licebse_file.setUrl(CommUtil.null2String(URI));
		} else {
			URI URI = uriFactory.createURI(this.configService.getSysConfig().getStoreImage().getUrl(), null, null);
			licebse_file.setUrl(CommUtil.null2String(URI));
		}
		storeApplicationVO.setLicebse_file(licebse_file);

		if (csa.getArea() != null) {
			AreaVO areaGrandParent = new AreaVO();
			AreaVO areaParent = new AreaVO();
			AreaVO area = new AreaVO();
			area.setId(csa.getArea().getId());
			area.setName(csa.getArea().getAreaName());
			if (csa.getArea().getParent() != null) {
				areaParent.setId(csa.getArea().getParent().getId());
				areaParent.setName(csa.getArea().getParent().getAreaName());
				area.setParent(areaParent);
				if (csa.getArea().getParent().getParent() != null) {
					areaGrandParent.setId(csa.getArea().getParent().getParent().getId());
					areaGrandParent.setName(csa.getArea().getParent().getParent().getAreaName());
					area.getParent().setParent(areaGrandParent);
				}
			}
			storeApplicationVO.setArea(area);
		}

		if (csa.getGrade() != null) {
			StoreGradeVO storeGradeVO = new StoreGradeVO();
			storeGradeVO.setId(csa.getGrade().getId());
			storeGradeVO.setGradeName(csa.getGrade().getGradeName());
			storeGradeVO.setPrice(csa.getGrade().getPrice());
			storeGradeVO.setGradeLevel(csa.getGrade().getGradeLevel());
			storeApplicationVO.setGrade(storeGradeVO);
		}

		if (csa.getSc() != null) {
			StoreClassVO storeClassVO = new StoreClassVO();
			storeClassVO.setId(csa.getSc().getId());
			storeClassVO.setClassName(csa.getSc().getClassName());
			storeClassVO.setLevel(csa.getSc().getLevel());
			storeApplicationVO.setStoreClass(storeClassVO);
		}

		storeApplicationVO.setStatus(parsestoreApplicationStatus(csa.getApply_status()));

		return storeApplicationVO;
	}

	private StoreApplicationStatus parsestoreApplicationStatus(int status_int) {
		StoreApplicationStatus status = null;
		switch (status_int) {
			case 0:
				status = StoreApplicationStatus.unaudited;
				break;
			case 1:
				status = StoreApplicationStatus.audited;
				break;
			case 2:
				status = StoreApplicationStatus.newAudited;
				break;
		}
		return status;
	}
}
