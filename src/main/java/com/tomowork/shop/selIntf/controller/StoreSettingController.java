package com.tomowork.shop.selIntf.controller;

import java.net.URISyntaxException;
import java.security.Principal;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.selIntf.entity.StoreSettingVO;
import com.tomowork.shop.selIntf.service.StoreSettinngService;
import com.tomowork.shop.selIntf.util.validator.Mobile;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
public class StoreSettingController {

	@Autowired
	private StoreSettinngService storeSettinngService;

	/**
	 * 查看店铺设置
	 * @param principal 用来获取登陆用户的用户名
	 * @return 店铺设置
	 */
	@RequestMapping(value = "/store/setting", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getStoreSetting(Principal principal) {
		StoreSettingVO storeSettingVO = this.storeSettinngService.getStoreSeting(principal.getName());
		return new ResponseEntity<>(storeSettingVO, HttpStatus.OK);
	}

	/**
	 * 修改店铺设置
	 * @param storeSettingVO 店铺设置类
	 * @param principal 用来获取登陆用户的用户名
	 * @return httpStatus
	 */
	@RequestMapping(value = "/store/setting", method = RequestMethod.PATCH)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> alterStoreSetting(@RequestBody @Valid StoreSettingRequestVO storeSettingVO, Principal principal) {
		this.storeSettinngService.alterStoreSetting(storeSettingVO, principal.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 修改店铺设置图片
	 * @param principal 用来获取登陆用户的用户名
	 * @param store_logo 身份证正面照片文件
	 * @param store_banner 身份证背面照片文件
	 * @return HttpStatus
	 * @throws URISyntaxException URI不正确
	 */
	@RequestMapping(value = "/store/storeSettinngImages", method = RequestMethod.POST)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> storeImages(Principal principal,
			@RequestParam(value = "store_logo", required = false) MultipartFile store_logo,
			@RequestParam(value = "store_banner", required = false) MultipartFile store_banner)
			throws URISyntaxException {
		this.storeSettinngService.storeSettinngImages(principal.getName(), store_logo, store_banner);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	static class StoreSettingRequestVO extends StoreSettingVO {

		@Override
		@Pattern(regexp = "^[1-9][0-9]\\d{3,9}$", message = "请输入正确的QQ号码")
		public String getStore_qq() {
			return super.getStore_qq();
		}

		@Override
		@Mobile
		public String getStore_telephone() {
			return super.getStore_telephone();
		}

		@Override
		@Pattern(regexp = "^\\d{6}$", message = "邮政编码格式为6位的数字")
		public String getStore_zip() {
			return super.getStore_zip();
		}
	}
}
