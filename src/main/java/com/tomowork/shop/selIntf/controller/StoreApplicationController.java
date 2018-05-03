package com.tomowork.shop.selIntf.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.api.AreaVO;
import com.tomowork.shop.selIntf.entity.StoreApplicationVO;
import com.tomowork.shop.selIntf.entity.StoreClassVO;
import com.tomowork.shop.selIntf.entity.StoreGradeVO;
import com.tomowork.shop.selIntf.service.StoreApplicationService;
import com.tomowork.shop.selIntf.util.validator.Mobile;
import com.tomowork.spring.web.bind.annotation.ApiVersion;

@RestController
public class StoreApplicationController {

	@Autowired
	private StoreApplicationService createStoreService;

	/**
	 * 申请店铺
	 * @param principal 用来获取登陆用户的用户名
	 * @param createStoreVO 申请店铺信息
	 * @return HttpStatus
	 * @throws URISyntaxException URI不正确
	 */
	@RequestMapping(value = "/storeApplication", method = RequestMethod.POST)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> createStoreApplication(@RequestBody @Valid CreateStoreRequestVO createStoreVO, Principal principal) throws URISyntaxException {
		Long createStoreApplicationId =
				this.createStoreService.createStoreApplication(createStoreVO, principal.getName());
		return ResponseEntity.created(new URI("/store/" + createStoreApplicationId)).build();
	}

	/**
	 * 申请店铺添加认证信息
	 * @param principal 用来获取登陆用户的用户名
	 * @param card_front 身份证正面照片文件
	 * @param card_side 身份证背面照片文件
	 * @param card_hand 身份证手持照片文件
	 * @param licebse_file 营业执照照片文件
	 * @return HttpStatus
	 * @throws URISyntaxException URI不正确
	 */
	@RequestMapping(value = "/storeApplication/authentication", method = RequestMethod.POST)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> imagesAuthenticate(Principal principal,
			@RequestParam(value = "card_front", required = false) MultipartFile card_front,
			@RequestParam(value = "card_side", required = false) MultipartFile card_side,
			@RequestParam(value = "card_hand", required = false) MultipartFile card_hand,
			@RequestParam(value = "licebse_file", required = false) MultipartFile licebse_file)
			throws URISyntaxException {
		Long CreateStoreApplicationId = this.createStoreService.imagesAuthenticate(principal.getName(), card_front, card_side,
				card_hand, licebse_file);
		return ResponseEntity.created(new URI("/store/" + CreateStoreApplicationId)).build();
	}

	/**
	 * 查看申请店铺详情
	 * @param principal 用来获取登陆用户的用户名
	 * @return 店铺申请记录
	 */
	@RequestMapping(value = "/storeApplication/details", method = RequestMethod.GET)
	@ApiVersion(major = 1)
	public ResponseEntity<Object> getStoreApplication(Principal principal) {
		StoreApplicationVO storeApplicationVO = this.createStoreService.getStoreApplication(principal.getName());
		return new ResponseEntity<>(storeApplicationVO, HttpStatus.OK);
	}

	static class CreateStoreRequestVO extends StoreApplicationVO {

		@Override
		@NotNull(message = "店铺名不能为空")
		@Size(max = 20, message = "店铺名最大不能超过20字符")
		public String getStore_name() {
			return super.getStore_name();
		}

		@Override
		@NotNull(message = "店主名不能为空")
		public String getStore_ower() {
			return super.getStore_ower();
		}

		@Override
		@NotNull(message = "身份证不能为空")
		public String getStore_ower_card() {
			return super.getStore_ower_card();
		}

		@Override
		@NotNull(message = "QQ号码不能为空")
		@Pattern(regexp = "^[1-9][0-9]\\d{3,9}$", message = "请输入正确的QQ号码")
		public String getStore_qq() {
			return super.getStore_qq();
		}

		@Override
		@NotNull(message = "电话号码不能为空")
		@Mobile
		public String getStore_telephone() {
			return super.getStore_telephone();
		}

		@Override
		@NotNull(message = "邮政编码不能为空")
		@Pattern(regexp = "^\\d{6}$", message = "邮政编码格式为6位的数字")
		public String getStore_zip() {
			return super.getStore_zip();
		}

		@Override
		@NotNull(message = "地址不能为空")
		public String getStore_address() {
			return super.getStore_address();
		}

		@Override
		@NotNull(message = "区域不能为空")
		public AreaVO getArea() {
			return super.getArea();
		}

		@Override
		@NotNull(message = "无效店铺等级")
		public StoreGradeVO getGrade() {
			return super.getGrade();
		}

		@Override
		@NotNull(message = "无效店铺分类")
		public StoreClassVO getStoreClass() {
			return super.getStoreClass();
		}
	}
}
