package com.tomowork.shop.selIntf.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import com.tomowork.shop.foundation.domain.UserGoodsClass;
import com.tomowork.shop.selIntf.entity.UserGoodsClassVO;
import com.tomowork.shop.selIntf.service.MobileUserGoodsClassService;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
@Validated
public class MobileUserGoodsClassController {

	@Autowired
	private MobileUserGoodsClassService mobileUserGoodsClassService;

	/**
	 * 获取店铺商品分类
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/userGoodsClass", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getUserGoodsList(Principal principal) {
		List<UserGoodsClassVO> userGoodsClassVOList = this.mobileUserGoodsClassService.getUserGoodsClassList(principal.getName());
		return new ResponseEntity<>(userGoodsClassVOList, HttpStatus.OK);
	}

	/**
	 * 添加店铺商品分类
	 * @param userGoodsClassVO 店铺商品分类类
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/userGoodsClass", method = RequestMethod.POST)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> addUserGoods(@RequestBody @Valid UserGoodsClassVORequest userGoodsClassVO, Principal principal) throws URISyntaxException {
		UserGoodsClass userGoodsClass = this.mobileUserGoodsClassService.addUserGoodsClass(userGoodsClassVO, principal.getName());
		return ResponseEntity.created(new URI("/userGoodsClass/" + userGoodsClass.getId())).build();
	}

	/**
	 * 修改店铺商品分类
	 * @param userGoodsClassId 分类的id
	 * @param userGoodsClassVO 店铺商品分类类
	 * @param principal 用来获取登陆用户的用户名1
	 * @return httpStatus
	 */
	@RequestMapping(value = "/userGoodsClass/{id}", method = RequestMethod.PATCH)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> alterUserGoods(@PathVariable("id") Long userGoodsClassId,
			@RequestBody UserGoodsClassVO userGoodsClassVO, Principal principal) {
		this.mobileUserGoodsClassService.alterUserGoodsClass(userGoodsClassId, userGoodsClassVO, principal.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 删除店铺商品分类
	 * @param userGoodsClassId 分类的id
	 * @param principal 用来获取登陆用户的用户名
	 * @return httpStatus
	 */
	@RequestMapping(value = "/userGoodsClass/{id}", method = RequestMethod.DELETE)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> delrUserGoods(@PathVariable("id") Long userGoodsClassId, Principal principal) {
		this.mobileUserGoodsClassService.delUserGoodsClass(userGoodsClassId, principal.getName());
		return ResponseEntity.noContent().build();
	}

	/**
	 * 按照分类获取商品
	 * @return 商品列表
	 */
	@RequestMapping(value = "/userGoodsClass/{id}", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getGoodsByUserGoodsClass(@PathVariable("id") Long userGoodsClassId) {
		List<GoodsVO> goodsVOList = this.mobileUserGoodsClassService.getGoodsByUserGoodsClass(userGoodsClassId);
		return new ResponseEntity<>(goodsVOList, HttpStatus.OK);
	}

	static class UserGoodsClassVORequest extends  UserGoodsClassVO {

		@Override
		@NotNull(message = "分类名称不能为空")
		public String getClassName() {
			return super.getClassName();
		}

		@Override
		@NotNull(message = "显示状态不能为空")
		public Boolean getDisplay() {
			return super.getDisplay();
		}
	}
}
