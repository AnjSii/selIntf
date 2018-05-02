package com.tomowork.shop.selIntf.controller;

import java.security.Principal;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.selIntf.service.MoblieStoreSlideService;
import com.tomowork.shop.selIntf.util.validator.Url;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
@Validated
public class StoreSlideController {

	@Autowired
	private MoblieStoreSlideService moblieStoreSlideServicel;

	/**
	 * 设置店铺幻灯片
	 * @param principal 用来获取登陆用户的用户名
	 * @param slideIndex 幻灯片位置
	 * @param slideFile 幻灯片图片文件
	 * @param url 幻灯片点击跳转的地址
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/storeSlide", method = RequestMethod.POST)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> storeSlide(@RequestParam("slideIndex") @Max(value = 5, message = "超出幻灯片索引") int slideIndex,
			@RequestParam("slideFile") @NotNull MultipartFile slideFile, @RequestParam(value = "url", required = false) @Url String url, Principal principal) {
		this.moblieStoreSlideServicel.addStroeSlide(slideIndex, slideFile, url, principal.getName());
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
}
