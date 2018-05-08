package com.tomowork.shop.selIntf.service;

import java.util.List;

import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;

public interface MobileGoodsSoldService {

	/**
	 * 获取所有正在出售的商品
	 * @param userName 用户名
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 没有查询到店铺
	 * @throws ViolationException 店铺正在审核中 店铺已经被关闭 店铺审核被拒绝
	 */
	List<GoodsVO> getGoodsSold(String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException;

	/**
	 * 下架正在出售的商品
	 * @param goodsId 商品ID
	 * @param userName 用户名
	 * @throws NullPointerException 用户名为空 商品ID为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 没有查询到店铺 没有查询到商品
	 * @throws ViolationException 店铺正在审核中 店铺已经被关闭 店铺审核被拒绝
	 */
	void putInWarehouse(Long goodsId, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException;
}
