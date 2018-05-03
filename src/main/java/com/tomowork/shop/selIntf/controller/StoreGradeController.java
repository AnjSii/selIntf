package com.tomowork.shop.selIntf.controller;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomowork.shop.selIntf.entity.StoreGradeVO;
import com.tomowork.shop.selIntf.service.MobileStoreGradeService;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
public class StoreGradeController {

	@Autowired
	private MobileStoreGradeService mobileStoreGradeService;

	/**
	 * 获取所有店铺等级
	 * @param principal 用来获取登陆用户的用户名
	 * @return 店铺设置等级列表
	 */
	@RequestMapping(value = "/storeGradnes", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getsSoreGrade(Principal principal) {
		List<StoreGradeVO> storeGradeVOList = this.mobileStoreGradeService.getStoreGradeList(principal.getName());
		return new ResponseEntity<>(storeGradeVOList, HttpStatus.OK);
	}

	/**
	 * 申请店铺等级
	 * @param principal 用来获取登陆用户的用户名
	 * @param storeGradeId 店铺等级Id
	 * @return HttpStatus
	 * @throws URISyntaxException URI不正确
	 */
	@RequestMapping(value = "/storeGrade", method = RequestMethod.POST)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> applyStoreGradnes(@RequestParam("storeGradeId") Long storeGradeId, Principal principal) throws URISyntaxException {
		this.mobileStoreGradeService.applyGrade(storeGradeId, principal.getName());
		return ResponseEntity.noContent().build();
	}
}
