package com.tomowork.shop.selIntf.service;

import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.selIntf.entity.StoreApplicationVO;
import com.tomowork.shop.selIntf.exception.ViolationException;

public interface StoreApplicationService {

	/**
	 * 申请店铺
	 * @param storeApplicationVO 申请店铺类
	 * @param userName 用户名
	 * @return Long CreateStoreApplication.id 开店申请记录的id
	 * @throws NullPointerException createStoreVO createStoreVO里面的AreaVO区域类为空 createStoreVO里面的AreaVO区域类里面的id为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws ViolationException 店铺名已经存 无效店铺等级 使用createStoreVO里面的Area的id查询出来的区域不存在 无效店铺分类
	 * 店铺正在审核中 已经开通店铺 店铺已经被关闭 店铺已经过期关闭 店铺审核被拒绝
	 */
	Long createStoreApplication(StoreApplicationVO storeApplicationVO, String userName) throws NullPointerException, IllegalArgumentException, ViolationException;

	/**
	 * 申请店铺添加认证信息
	 * @param card_front 身份证正面图片文件
	 * @param card_side 身份证背面图片文件
	 * @param card_hand 身份证手持图片文件
	 * @param licebse_file 营业执照图片文件
	 * @param userName 用户名
	 * @return 申请店铺记录id
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws ViolationException 没有申请记录 获取文件名为空字符串
	 */
	Long imagesAuthenticate(String userName, MultipartFile card_front, MultipartFile card_side, MultipartFile card_hand,
							MultipartFile licebse_file) throws NullPointerException, IllegalArgumentException, ViolationException;

	/**
	 * 申请店铺查看申请信息
	 * @param userName 用户名
	 * @return 申请店铺记录详情
	 * @throws NullPointerException 获取文件为空 取文件名后缀为空
	 * @throws IllegalArgumentException 用户名为空字符串 获取文件名为空字符串
	 * @throws ViolationException 获取文件名为空字符串
	 */
	StoreApplicationVO getStoreApplication(String userName) throws NullPointerException, IllegalArgumentException;
}
