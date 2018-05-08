package com.tomowork.shop.selIntf.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.selIntf.service.MobileGoodsSoldService;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
@Validated
public class MobileGoodsSoldController {

	@Autowired
	private MobileGoodsSoldService mobileGoodsSoldService;

	/**
	 * 获取店铺品牌
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsSold", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getGoodsSoldList(Principal principal) {
		List<GoodsVO> goodsVOList = this.mobileGoodsSoldService.getGoodsSold(principal.getName());
		return new ResponseEntity<>(goodsVOList, HttpStatus.OK);
	}

	/**
	 * 出售中的商品下架
	 * @param goodsId 商品id
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsSold/{id}", method = RequestMethod.PUT)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> putInWarehouse(@PathVariable("id") Long goodsId, Principal principal) {
		this.mobileGoodsSoldService.putInWarehouse(goodsId, principal.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
