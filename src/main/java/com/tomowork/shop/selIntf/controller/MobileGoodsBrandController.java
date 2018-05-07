package com.tomowork.shop.selIntf.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.foundation.domain.GoodsBrand;
import com.tomowork.shop.selIntf.entity.GoodsBrandVO;
import com.tomowork.shop.selIntf.service.MobileGoodsBrandService;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
@Validated
public class MobileGoodsBrandController {

	@Autowired
	private MobileGoodsBrandService mobileGoodsBrandService;

	/**
	 * 获取店铺品牌
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsBrands", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getgoodsBrandList(Principal principal) {
		List<GoodsBrandVO> goodsBrandVOList = this.mobileGoodsBrandService.getGoodsBrandList(principal.getName());
		return new ResponseEntity<>(goodsBrandVOList, HttpStatus.OK);
	}

	/**
	 * 添加店铺品牌
	 * @param goodsBrandVO 品牌类
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsBrand", method = RequestMethod.POST)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> addGoodsBrand(@RequestBody @Valid GoodsBrandVORequest goodsBrandVO, Principal principal)
			throws URISyntaxException {
		GoodsBrand goodsBrand = this.mobileGoodsBrandService.addGoodsBrand(goodsBrandVO, principal.getName());
		return ResponseEntity.created(new URI("/goodsBrand/" + goodsBrand.getId())).build();
	}

	/**
	 * 添加店铺品牌logo
	 * @param brandLogo 品牌logo文件
	 * @param principal 用来获取登陆用户的用户名
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/goodsBrand/{id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> addGoodsBrandLogo(@PathVariable("id") Long goodsBrandId,
			@RequestParam("brandLogo") @NotNull(message = "品牌标志不能为空") MultipartFile brandLogo,
			Principal principal) throws URISyntaxException {
			this.mobileGoodsBrandService.addGoodsBrandLogo(goodsBrandId, brandLogo, principal.getName());
		return ResponseEntity.noContent().build();
	}

	/**
	 * 修改店铺品牌
	 * @param goodsBrandId 品牌的id
	 * @param goodsBrandVO 品牌类
	 * @param principal 用来获取登陆用户的用户名
	 * @return httpStatus
	 */
	@RequestMapping(value = "/goodsBrand/{id}", method = RequestMethod.PATCH)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> alterGoodsBrand(@PathVariable("id") Long goodsBrandId,
			@RequestBody @NotNull(message = "修改信息不能为空") GoodsBrandVO goodsBrandVO, Principal principal) {
		this.mobileGoodsBrandService.alterGoodsBrand(goodsBrandId, goodsBrandVO, principal.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 删除店铺品牌
	 * @param goodsBrandId 品牌的id\
	 * @param principal 用来获取登陆用户的用户名
	 * @return httpStatus
	 */
	@RequestMapping(value = "/goodsBrand/{id}", method = RequestMethod.DELETE)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> delGoodsBrand(@PathVariable("id") Long goodsBrandId, Principal principal) {
		this.mobileGoodsBrandService.delGoodsBrand(goodsBrandId, principal.getName());
		return ResponseEntity.noContent().build();
	}

	static class GoodsBrandVORequest extends GoodsBrandVO {

		@Override
		@NotNull(message = "品牌名称不能为空")
		@Size(min = 1, message = "请输入品牌名称")
		public String getName() {
			return super.getName();
		}

		@Override
		@NotNull(message = "品牌首字母不能为空")
		@Size(min = 1, message = "请输入品牌首字母")
		public String getFirst_word() {
			return super.getFirst_word();
		}
	}
}
