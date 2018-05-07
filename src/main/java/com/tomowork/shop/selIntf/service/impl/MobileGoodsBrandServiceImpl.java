package com.tomowork.shop.selIntf.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.assets.tools.ImageType;
import com.tomowork.shop.core.tools.AccessoryUtil;
import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.foundation.domain.Accessory;
import com.tomowork.shop.foundation.domain.GoodsBrand;
import com.tomowork.shop.foundation.domain.SysConfig;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.AccessoryService;
import com.tomowork.shop.foundation.service.GoodsBrandService;
import com.tomowork.shop.foundation.service.SysConfigService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.entity.GoodsBrandVO;
import com.tomowork.shop.selIntf.entity.GoodsBrandVO.GodosBrandStatus;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.MobileGoodsBrandService;
import com.tomowork.shop.velocity.tools.view.URIFactory;

@Service
@Transactional
public class MobileGoodsBrandServiceImpl implements MobileGoodsBrandService {

	@Autowired
	private UserService userService;

	@Autowired
	private GoodsBrandService goodsBrandService;

	@Autowired
	@Named("imageUriFactory")
	URIFactory uriFactory;

	@Autowired
	private SysConfigService configService;

	@Autowired
	private AccessoryService accessoryService;

	@Override
	public List<GoodsBrandVO> getGoodsBrandList(String userName) throws NullPointerException, IllegalArgumentException {

		if (userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		HashMap<String, Object> params = new HashMap<>();
		params.put("userId", user.getId());
		List<GoodsBrand> goodsBrandList = this.goodsBrandService
				.query("select obj from GoodsBrand obj where obj.user.id = :userId order by obj.addTime desc",
						params, -1, -1);

		List<GoodsBrandVO> goodsBrandVOList = new ArrayList<>();
		for (GoodsBrand goodsBrand : goodsBrandList) {
			GoodsBrandVO goodsBrandVO = new GoodsBrandVO();
			goodsBrandVO.setId(goodsBrand.getId());
			goodsBrandVO.setName(goodsBrand.getName());
			goodsBrandVO.setFirst_word(goodsBrand.getFirst_word());
			goodsBrandVO.setSequence(goodsBrand.getSequence());
			goodsBrandVO.setAudit(parseGodosBrandStatus(goodsBrand.getAudit()));
			goodsBrandVO.setRemark(goodsBrand.getRemark());

			if (goodsBrand.getBrandLogo() != null) {
				URI uri = uriFactory.createURI(goodsBrand.getBrandLogo().getUrl(), null, null);
				goodsBrandVO.setBrandLogo_url(uri.toString());
			} else {
				SysConfig config = this.configService.getSysConfig();
				URI uri = uriFactory.createURI(config.getGoodsImage().getUrl(), null, null);
				goodsBrandVO.setBrandLogo_url(uri.toString());
			}
			goodsBrandVOList.add(goodsBrandVO);
		}
		return goodsBrandVOList;
	}

	@Override
	public GoodsBrand addGoodsBrand(GoodsBrandVO goodsBrandVO, String userName) throws NullPointerException, IllegalArgumentException {

		if (userName == null || goodsBrandVO == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		GoodsBrand goodsBrand = new GoodsBrand();
		goodsBrand.setName(goodsBrandVO.getName());
		goodsBrand.setFirst_word(goodsBrandVO.getFirst_word());
		goodsBrand.setUser(user);
		goodsBrand.setUserStatus(1);
		goodsBrand.setAddTime(new Date());
		if (goodsBrandVO.getRemark() != null && !goodsBrandVO.getRemark().isEmpty()) {
			goodsBrand.setRemark(goodsBrandVO.getRemark());
		}
		this.goodsBrandService.save(goodsBrand);

		return goodsBrand;
	}

	@Override
	public GoodsBrand addGoodsBrandLogo(Long goodsBrandId, MultipartFile brandLogo, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException {

		if (goodsBrandId == null || brandLogo == null || brandLogo.getOriginalFilename() == null || brandLogo.getOriginalFilename().lastIndexOf(".") == -1) {
			throw new NullPointerException();
		}
		if (brandLogo.isEmpty() || brandLogo.getOriginalFilename().isEmpty()) {
			throw new IllegalArgumentException();
		}

		String imageType = brandLogo.getOriginalFilename().substring(brandLogo.getOriginalFilename()
				.lastIndexOf(".") + 1).toLowerCase();
		ImageType shopImageType = ImageType.typeOf(imageType);
		if (shopImageType == null) {
			throw new ViolationException("图片格式不正确");
		}

		User user = this.userService.getUserByUsername(userName);
		GoodsBrand goodsBrand = this.goodsBrandService.getObjById(goodsBrandId);
		if (goodsBrand == null) {
			throw new EntityNotFoundException("未找到品牌");
		}
		if (!user.getId().equals(goodsBrand.getUser().getId())) {
			throw new ViolationException("品牌与当前用户不匹配");
		}

		String path = AccessoryUtil.getUploadPath(this.configService.getSysConfig().getUploadFilePath(), null, "brand");
		try (InputStream is = brandLogo.getInputStream()) {
			String[] type = {brandLogo.getOriginalFilename().substring(brandLogo.getOriginalFilename()
					.lastIndexOf(".") + 1).toLowerCase().trim()};

			Map<String, Object> map = CommUtil.saveFileToServer(path, null, is, type);
			if (!"".equals(map.get("fileName"))) {
				Accessory accessory = new Accessory();
				accessory.setName(CommUtil.null2String(map.get("fileName")));
				accessory.setExt(CommUtil.null2String(map.get("mime")));
				accessory.setSize(CommUtil.null2Float(map.get("fileSize")));
				accessory.setPath("brand");
				accessory.setWidth(CommUtil.null2Int(map.get("width")));
				accessory.setHeight(CommUtil.null2Int(map.get("height")));
				accessory.setAddTime(new Date());
				accessory.setUser(user);
				this.accessoryService.save(accessory);
				goodsBrand.setBrandLogo(accessory);
			}
		} catch (IOException e) {
			throw new ViolationException("图片格式不正确");
		}
		this.goodsBrandService.update(goodsBrand);

		return goodsBrand;
	}

	@Override
	public void alterGoodsBrand(Long goodsBrandId, GoodsBrandVO goodsBrandVO, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException {
		if (goodsBrandId == null || goodsBrandVO == null || userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		GoodsBrand goodsBrand = this.goodsBrandService.getObjById(goodsBrandId);
		if (goodsBrand == null) {
			throw new EntityNotFoundException("没有查询到所要修改的分类");
		}
		if (!(goodsBrand.getUser().getId().equals(user.getId()))) {
			throw new EntityNotFoundException("分类与用户不匹配");
		}

		if (goodsBrandVO.getName() != null && !goodsBrandVO.getName().isEmpty()) {
			goodsBrand.setName(goodsBrandVO.getName());
		}

		if (goodsBrandVO.getFirst_word() != null && !goodsBrandVO.getFirst_word().isEmpty()) {
			goodsBrand.setFirst_word(goodsBrandVO.getFirst_word());
		}
		if (goodsBrandVO.getRemark() != null && !goodsBrandVO.getRemark().isEmpty()) {
			goodsBrand.setRemark(goodsBrandVO.getRemark());
		}
		this.goodsBrandService.update(goodsBrand);
	}

	@Override
	public void delGoodsBrand(Long goodsBrandId, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException {
		if (goodsBrandId == null || userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		GoodsBrand goodsBrand = this.goodsBrandService.getObjById(goodsBrandId);
		if (goodsBrand == null) {
			throw new EntityNotFoundException("没有查询到所要修改的分类");
		}
		if (!(goodsBrand.getUser().getId().equals(user.getId()))) {
			throw new EntityNotFoundException("分类与用户不匹配");
		}

		if (goodsBrand.getAudit() != 1) {
			Accessory brandLogo = goodsBrand.getBrandLogo();
			if (this.goodsBrandService.delete(goodsBrandId)) {
				if (brandLogo != null) {
					brandLogo.setDeleteStatus(true);
					accessoryService.update(brandLogo);
				}
			}
		}
	}

	private GodosBrandStatus parseGodosBrandStatus(int status_int) {
		GodosBrandStatus status = null;
		switch (status_int) {
			case 0:
				status = GodosBrandStatus.newAudited;
				break;
			case 1:
				status = GodosBrandStatus.audited_success;
				break;
			case -1:
				status = GodosBrandStatus.audited_fail;
				break;
		}
		return status;
	}
}
