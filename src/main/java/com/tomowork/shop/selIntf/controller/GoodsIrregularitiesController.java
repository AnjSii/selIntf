package com.tomowork.shop.selIntf.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.selIntf.entity.AppealReasnRequestVO;
import com.tomowork.shop.selIntf.service.GoodsIrregularitiesService;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
@Validated
public class GoodsIrregularitiesController {

	@Autowired
	private GoodsIrregularitiesService getGoodsInWarehouse;

	/**
	 * 获取违规下架的商品
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsIrregularities", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getGoodsSoldList(Principal principal) {
		List<GoodsVO> goodsVOList = this.getGoodsInWarehouse.getGoodsIrregularities(principal.getName());
		return new ResponseEntity<>(goodsVOList, HttpStatus.OK);
	}

	/**
	 * 违规下架的商品申诉
	 * @param goodsId 商品id
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsIrregularities/{id}", method = RequestMethod.PUT)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> goodsAppeal(@PathVariable("id") Long goodsId, @RequestBody @Valid AppealReasnRequestVO appealReasnRequestVO, Principal principal) {
		this.getGoodsInWarehouse.goodsAppeal(goodsId, appealReasnRequestVO, principal.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
