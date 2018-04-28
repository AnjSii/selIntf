package com.tomowork.shop.selIntf.service;

import java.util.List;

import com.tomowork.shop.selIntf.entity.StoreGradeVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;

public interface MobileStoreGradeService {

	/**
	 * 获取所有店铺等级
	 * @param userName 用户名
	 * @return 店铺等级列表
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 没有店铺
	 * @throws ViolationException 店铺正在审核中 店铺已经被关闭 店铺已经过期关闭 店铺审核被拒绝
	 */
	List<StoreGradeVO> getStoreGradeList(String userName) throws NullPointerException, IllegalArgumentException,
			EntityNotFoundException, ViolationException;

	/**
	 * 申请店铺升级
	 * @param storeGradeId 店铺等级Id
	 * @param userName 用户名
	 * @return 店铺Id
	 * @throws NullPointerException 用户名为空 店铺等级Id为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 没有店铺
	 * @throws ViolationException 店铺正在审核中 店铺已经被关闭 店铺已经过期关闭 店铺审核被拒绝 申请等级与当前等级相同 店铺升级正在审核中
	 */
	Long applyGrade(Long storeGradeId, String userName) throws NullPointerException, IllegalArgumentException, ViolationException;
}
