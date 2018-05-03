package com.tomowork.shop.selIntf.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.assets.tools.ImageType;
import com.tomowork.shop.core.tools.AccessoryUtil;
import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.foundation.domain.Accessory;
import com.tomowork.shop.foundation.domain.Store;
import com.tomowork.shop.foundation.domain.StoreSlide;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.AccessoryService;
import com.tomowork.shop.foundation.service.StoreSlideService;
import com.tomowork.shop.foundation.service.SysConfigService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.MoblieStoreSlideService;

@Service
@Transactional
public class MoblieStoreSlideServiceImpl implements MoblieStoreSlideService {

	@Autowired
	private UserService userService;

	@Autowired
	private SysConfigService configService;

	@Autowired
	private AccessoryService accessoryService;

	@Autowired
	private StoreSlideService storeSlideService;

	@Override
	public void addStroeSlide(int slideIndex, MultipartFile slideFile, String url, String userName) throws NullPointerException, IllegalArgumentException,
			EntityNotFoundException, ViolationException {
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

		String imageType = slideFile.getOriginalFilename().substring(slideFile.getOriginalFilename()
				.lastIndexOf(".") + 1).toLowerCase();
		ImageType shopImageType = ImageType.typeOf(imageType);
		if (shopImageType == null) {
			throw new ViolationException("图片格式不正确");
		}

		if (slideIndex > 5) {
			throw new ViolationException("超出幻灯片索引");
		}

		StoreSlide slide = null;
		if (store.getSlides().size() > (slideIndex - 1)) {
			slide = store.getSlides().get(slideIndex - 1);
		}
		if (slide != null) {
			String path = AccessoryUtil.getUploadPath(this.configService.getSysConfig().getUploadFilePath(), null, "store_slide");
			try (InputStream is = slideFile.getInputStream()) {
				String[] type = {slideFile.getOriginalFilename().substring(slideFile.getOriginalFilename()
						.lastIndexOf(".") + 1).toLowerCase().trim()};
				Map<String, Object> map = CommUtil.saveFileToServer(path, null, is, type);
				if (!"".equals(map.get("fileName"))) {
					Accessory accessory = new Accessory();
					accessory.setName(CommUtil.null2String(map.get("fileName")));
					accessory.setExt(CommUtil.null2String(map.get("mime")));
					accessory.setSize(CommUtil.null2Float(map.get("fileSize")));
					accessory.setPath("store_slide");
					accessory.setWidth(CommUtil.null2Int(map.get("width")));
					accessory.setHeight(CommUtil.null2Int(map.get("height")));
					accessory.setAddTime(new Date());
					accessory.setUser(user);
					this.accessoryService.save(accessory);

					Accessory oldAcc = slide.getAcc();
					slide.setAcc(accessory);
					slide.setUrl(url);
					slide.setDeleteStatus(false);
					if (storeSlideService.update(slide) && oldAcc != null) {
						oldAcc.setDeleteStatus(true);
						accessoryService.update(oldAcc);
					}
				}
			} catch (IOException e) {
				throw new ViolationException("图片格式不正确");
			}
		} else {
			String path = AccessoryUtil.getUploadPath(this.configService.getSysConfig().getUploadFilePath(), null, "store_slide");
			try (InputStream is = slideFile.getInputStream()) {
				String[] type = {slideFile.getOriginalFilename().substring(slideFile.getOriginalFilename()
						.lastIndexOf(".") + 1).toLowerCase().trim()};
				Map<String, Object> map = CommUtil.saveFileToServer(path, null, is, type);
				slide = new StoreSlide();
				slide.setAddTime(new Date());
				slide.setStore(store);
				slide.setUrl(url);
				if (!"".equals(map.get("fileName"))) {
					Accessory acc = new Accessory();
					acc.setName(CommUtil.null2String(map.get("fileName")));
					acc.setExt(CommUtil.null2String(map.get("mime")));
					acc.setSize(CommUtil.null2Float(map.get("fileSize")));
					acc.setPath("store_slide");
					acc.setWidth(CommUtil.null2Int(map.get("width")));
					acc.setHeight(CommUtil.null2Int(map.get("height")));
					acc.setAddTime(new Date());
					acc.setUser(user);
					slide.setAcc(acc);
					this.accessoryService.save(acc);
				} else {
					slide.setAcc(null);
					slide.setDeleteStatus(true);
				}
				storeSlideService.save(slide);
			} catch (IOException e) {
				throw new ViolationException("图片格式不正确");
			}
		}
	}
}
