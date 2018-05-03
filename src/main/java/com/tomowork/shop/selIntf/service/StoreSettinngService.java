package com.tomowork.shop.selIntf.service;

import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.selIntf.entity.StoreSettingVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;

public interface StoreSettinngService {

	/**
	 * 获取店铺设置信息
	 * @param userName 用户名
	 * @return 店铺设置信息
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 没有店铺
	 * @throws ViolationException 店铺正在审核中 店铺已经被关闭 店铺已经过期关闭 店铺审核被拒绝
	 */
	StoreSettingVO getStoreSeting(String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException,
			ViolationException;

	/**
	 * 修改店铺设置信息
	 * @param storeSettingVO 店铺设置类
	 * @param userName 用户名
	 * @throws NullPointerException 店铺设置类为空 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 店铺没有找到\还没有申请店铺
	 * @throws ViolationException 店铺正在审核中 店铺已经被关闭 店铺已经过期关闭 店铺审核被拒绝
	 */
	void alterStoreSetting(StoreSettingVO storeSettingVO, String userName) throws NullPointerException, IllegalArgumentException,
			EntityNotFoundException, ViolationException;

	/**
	 * 店铺图片设置
	 * @param store_logo 店铺logo图片文件
	 * @param store_banner 店铺条幅图片文件
	 * @param userName 用户名
	 * @return 申请店铺记录id
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 铺没有找到\还没有申请店铺
	 * @throws ViolationException 图片格式不正确
	 */
	void storeSettinngImages(String userName, MultipartFile store_logo, MultipartFile store_banner) throws NullPointerException, IllegalArgumentException, ViolationException;
}
