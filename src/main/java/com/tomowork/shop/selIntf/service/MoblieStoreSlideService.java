package com.tomowork.shop.selIntf.service;

import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.selIntf.entity.StoreSlideRequestVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;

public interface MoblieStoreSlideService {

	/**
	 * 申请店铺添加认证信息
	 * @param storeSlideRequestVO 幻灯片类
	 * @param userName 用户名
	 * @return 申请店铺记录id
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 没有店铺
	 * @throws ViolationException 必须上传至少一张幻灯片图片 图片格式不正确
	 */
	Long stroeSlide(StoreSlideRequestVO storeSlideRequestVO, String userName) throws NullPointerException, IllegalArgumentException,
			EntityNotFoundException, ViolationException;

	/**
	 * 申请店铺添加认证信息
	 * @param slideIndex 幻灯片位置
	 * @param slideFile 幻灯片图片文件
	 * @param url 点击幻灯片跳转地址
	 * @param userName 用户名
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 没有店铺
	 * @throws ViolationException 必须上传至少一张幻灯片图片 图片格式不正确
	 */
	void addStroeSlide(int slideIndex, MultipartFile slideFile, String url, String userName) throws NullPointerException, IllegalArgumentException,
			EntityNotFoundException, ViolationException;

}
