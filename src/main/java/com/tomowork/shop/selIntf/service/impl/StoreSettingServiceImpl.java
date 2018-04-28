package com.tomowork.shop.selIntf.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
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
import com.tomowork.shop.foundation.domain.Store;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.AccessoryService;
import com.tomowork.shop.foundation.service.AreaService;
import com.tomowork.shop.foundation.service.StoreService;
import com.tomowork.shop.foundation.service.SysConfigService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.entity.StoreGradeVO;
import com.tomowork.shop.selIntf.entity.StoreSettingVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.StoreSettinngService;
import com.tomowork.shop.velocity.tools.view.URIFactory;

@Service
@Transactional
public class StoreSettingServiceImpl implements StoreSettinngService {

	@Autowired
	private UserService userService;

	@Autowired
	private SysConfigService configService;

	@Autowired
	@Named("imageUriFactory")
	URIFactory uriFactory;

	@Autowired
	private StoreService storeService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private AccessoryService accessoryService;

	@Override
	public StoreSettingVO getStoreSeting(String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException {

		if (userName == null) {
			throw  new NullPointerException();
		}

		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		Store store = user.getStore();
		if (store == null) {
			throw new EntityNotFoundException("您还没有申请店铺");
		}
		StoreSettingVO storeSettingVO = new StoreSettingVO();
		int store_status = store.getStore_status();
		switch (store_status) {
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				storeSettingVO.setId(store.getId());
				storeSettingVO.setStore_qq(store.getStore_qq());
				storeSettingVO.setStore_seo_keywords(store.getStore_seo_keywords());
				storeSettingVO.setStore_seo_description(store.getStore_seo_description());
				storeSettingVO.setStore_telephone(store.getStore_telephone());
				storeSettingVO.setStore_zip(store.getStore_zip());
				storeSettingVO.setAddress(store.getStore_address());
				storeSettingVO.setName(store.getStore_name());
				storeSettingVO.setStore_name(store.getStore_name());
				storeSettingVO.setStore_ower(store.getStore_ower());
				storeSettingVO.setStore_ower_card(store.getStore_ower_card());

				if (store.getGrade() != null) {
					StoreGradeVO storeGradeVO = new StoreGradeVO();
					storeGradeVO.setId(store.getGrade().getId());
					storeGradeVO.setGradeName(store.getGrade().getGradeName());
					storeGradeVO.setPrice(store.getGrade().getPrice());
					storeGradeVO.setGradeLevel(store.getGrade().getGradeLevel());
					storeSettingVO.setGrade(storeGradeVO);
				}

				if (store.getArea() != null) {
					AreaVO areaGrandParent = new AreaVO();
					AreaVO areaParent = new AreaVO();
					AreaVO area = new AreaVO();
					area.setId(store.getArea().getId());
					area.setName(store.getArea().getAreaName());
					if (store.getArea().getParent() != null) {
						areaParent.setId(store.getArea().getParent().getId());
						areaParent.setName(store.getArea().getParent().getAreaName());
						area.setParent(areaParent);
						if (store.getArea().getParent().getParent() != null) {
							areaGrandParent.setId(store.getArea().getParent().getParent().getId());
							areaGrandParent.setName(store.getArea().getParent().getParent().getAreaName());
							area.getParent().setParent(areaGrandParent);
						}
					}
					storeSettingVO.setArea(area);
				}

				ImageVO logo = new ImageVO();
				if (store.getStore_logo() != null) {
					URI URI = uriFactory.createURI(store.getStore_logo().getUrl(), null, null);
					logo.setUrl(CommUtil.null2String(URI));
				} else {
					URI URI = uriFactory.createURI(this.configService.getSysConfig().getStoreImage().getUrl(), null, null);
					logo.setUrl(CommUtil.null2String(URI));
				}
				storeSettingVO.setLogo(logo);

				ImageVO banner = new ImageVO();
				if (store.getStore_banner() != null) {
					URI URI = uriFactory.createURI(store.getStore_banner().getUrl(), null, null);
					banner.setUrl(CommUtil.null2String(URI));
				} else {
					URI URI = uriFactory.createURI(this.configService.getSysConfig().getStoreImage().getUrl(), null, null);
					banner.setUrl(CommUtil.null2String(URI));
				}
				storeSettingVO.setStore_banner(banner);

				storeSettingVO.setStore_info(store.getStore_info());
				break;
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
		return storeSettingVO;
	}

	@Override
	public void alterStoreSetting(StoreSettingVO storeSettingVO, String userName) throws NullPointerException, IllegalArgumentException,
			EntityNotFoundException, ViolationException {
		if (userName == null || storeSettingVO == null) {
			throw new NullPointerException();
		}

		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		Store store = user.getStore();
		if (store == null) {
			throw new EntityNotFoundException("您还没有申请店铺");
		}

		int store_status = store.getStore_status();
		switch (store_status) {
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				if (storeSettingVO.getArea() != null && storeSettingVO.getArea().getId() != null) {
					Area area = this.areaService.getObjById(storeSettingVO.getArea().getId());
					if (area == null) {
						throw new ViolationException("区域找不到");
					}
					store.setArea(area);
				}
				if (storeSettingVO.getAddress() != null) {
					store.setStore_address(storeSettingVO.getAddress());
				}
				if (storeSettingVO.getStore_zip() != null) {
					store.setStore_zip(storeSettingVO.getStore_zip());
				}
				if (storeSettingVO.getStore_telephone() != null) {
					store.setStore_telephone(storeSettingVO.getStore_telephone());
				}
				if (storeSettingVO.getStore_qq() != null) {
					store.setStore_qq(storeSettingVO.getStore_qq());
				}
				if (storeSettingVO.getStore_seo_keywords() != null) {
					store.setStore_seo_keywords(storeSettingVO.getStore_seo_keywords());
				}
				if (storeSettingVO.getStore_seo_description() != null) {
					store.setStore_seo_description(storeSettingVO.getStore_seo_description());
				}
				this.storeService.update(store);
				break;
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
	}

	@Override
	public Long storeSettinngImages(String userName, MultipartFile store_logo, MultipartFile store_banner) throws NullPointerException,
			IllegalArgumentException, EntityNotFoundException, ViolationException {
		if (userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		Store store = user.getStore();
		if (store == null) {
			throw new EntityNotFoundException("您还没有申请店铺");
		}
		Map<String, MultipartFile> fileMap = new HashMap<>();
		fileMap.put("store_logo", store_logo);
		fileMap.put("store_banner", store_banner);
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

		Accessory old_store_logo = store.getStore_logo();
		Accessory old_store_banner = store.getStore_banner();
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			if (entry.getValue() != null) {
				String path = AccessoryUtil.getUploadPath(this.configService.getSysConfig().getUploadFilePath(), null, entry.getKey());
				try (InputStream is = entry.getValue().getInputStream()) {
					String[] type = {entry.getValue().getOriginalFilename().substring(entry.getValue().getOriginalFilename()
							.lastIndexOf(".") + 1).toLowerCase().trim()};
					Map<String, Object> map = CommUtil.saveFileToServer(path, null, is, type);

					if (!"".equals(map.get("fileName"))) {
						Accessory accessory = new Accessory();
						accessory.setName(CommUtil.null2String(map.get("fileName")));
						accessory.setExt(CommUtil.null2String(map.get("mime")));
						accessory.setSize(CommUtil.null2Float(map.get("fileSize")));
						accessory.setPath(entry.getKey());
						accessory.setWidth(CommUtil.null2Int(map.get("width")));
						accessory.setHeight(CommUtil.null2Int(map.get("height")));
						accessory.setAddTime(new Date());
						accessory.setUser(user);
						this.accessoryService.save(accessory);

						if (entry.getKey().equals("store_logo")) {
							store.setStore_logo(accessory);
						} else if (entry.getKey().equals("store_banner")) {
							store.setStore_banner(accessory);
						}
					}
				} catch (IOException e) {
					throw new ViolationException("图片格式不正确");
				}
				if (this.storeService.update(store)) {
					if (old_store_logo != null && old_store_logo != store.getStore_logo()) {
						old_store_logo.setDeleteStatus(true);
						accessoryService.update(old_store_logo);
					}
					if (old_store_banner != null && old_store_banner != store.getStore_banner()) {
						old_store_banner.setDeleteStatus(true);
						accessoryService.update(old_store_banner);
					}
				}
			}
		}
		return store.getId();
	}
}
