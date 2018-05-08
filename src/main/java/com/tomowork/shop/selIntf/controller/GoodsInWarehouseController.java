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
import com.tomowork.shop.selIntf.service.GoodsInWarehouseService;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
@Validated
public class GoodsInWarehouseController {

	@Autowired
	private GoodsInWarehouseService goodsInWarehouseService;

	/**获取仓库中的商品
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsInWarehouse", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getGoodsSoldList(Principal principal) {
		List<GoodsVO> goodsVOList = this.goodsInWarehouseService.getGoodsInWarehouse(principal.getName());
		return new ResponseEntity<>(goodsVOList, HttpStatus.OK);
	}

	/**
	 * 上架仓库中的商品
	 * @param goodsId 商品id
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsInWarehouse/{id}", method = RequestMethod.PUT)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> putInWarehouse(@PathVariable("id") Long goodsId, Principal principal) {
		this.goodsInWarehouseService.goodsOnSale(goodsId, principal.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 删除仓库中的商品
	 * @param goodsId 商品id
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsInWarehouse/{id}", method = RequestMethod.DELETE)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> delGoodsOnSale(@PathVariable("id") Long goodsId, Principal principal) {
		this.goodsInWarehouseService.delGetGoodsInWarehouse(goodsId, principal.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
