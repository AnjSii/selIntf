package com.tomowork.shop.selIntf.service;

import java.util.List;

import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.selIntf.entity.AppealReasnRequestVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;

public interface GoodsIrregularitiesService {

	/**
	 * 获取所有违规下架的商品
	 * @param userName 用户名
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 没有查询到店铺
	 * @throws ViolationException 店铺正在审核中 店铺已经被关闭 店铺审核被拒绝
	 */
	List<GoodsVO> getGoodsIrregularities(String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException;

	/**
	 * 违规下架的商品申诉
	 * @param goodsId 商品id
	 * @param appealReasnRequestVO 申诉原因类
	 * @param userName 用户名
	 * @throws NullPointerException 用户名为空 申诉原因类为空
	 * @throws IllegalArgumentException 用户名为空字符串 申诉原因类中的原因为空字符串
	 * @throws EntityNotFoundException 没有查询到店铺
	 * @throws ViolationException 店铺正在审核中 店铺已经被关闭 店铺审核被拒绝
	 */
	void goodsAppeal(Long goodsId, AppealReasnRequestVO appealReasnRequestVO, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException;
}
