package com.tomowork.shop.selIntf.service;

import java.util.List;

import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.foundation.domain.UserGoodsClass;
import com.tomowork.shop.selIntf.entity.UserGoodsClassVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;

public interface MobileUserGoodsClassService {

	/**
	 * 获取店铺商品分类
	 * @param userName 用户名
	 * @return 店铺商品分类
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 */
	List<UserGoodsClassVO> getUserGoodsClassList(String userName) throws NullPointerException, IllegalArgumentException;

	/**
	 * 添加店铺商品分类
	 * @param userName 用户名
	 * @return 店铺商品分类
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws ViolationException 上级分类没找到
	 */
	UserGoodsClass addUserGoodsClass(UserGoodsClassVO userGoodsClassVO, String userName) throws NullPointerException, IllegalArgumentException, ViolationException;

	/**
	 * 修改店铺商品分类
	 * @param userGoodsClassId 商品分类的id
	 * @param userName 用户名
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 使用userGoodsId没有查询到所要修改的分类 使用userGoodsId查出来的分类与当前登陆用户不匹配
	 * @throws ViolationException 上级分类没找到
	 */
	void alterUserGoodsClass(Long userGoodsClassId, UserGoodsClassVO userGoodsClassVO, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException;

	/**
	 * 删除店铺商品分类
	 * @param userGoodsClassId 商品分类的id
	 * @param userName 用户名
	 * @throws NullPointerException userGoodsId 用户名为null
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 使用userGoodsId没有查询到所要删除的分类 使用userGoodsId查出来的分类与当前登陆用户不匹配
	 */
	void delUserGoodsClass(Long userGoodsClassId, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException;

	/**
	 * 根据分类获取商品
	 * @param userGoodsClassId 商品分类的id
	 * @throws NullPointerException userGoodsId 用户名为null
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 使用userGoodsId没有查询到所要删除的分类 使用userGoodsId查出来的分类与当前登陆用户不匹配
	 */
	List<GoodsVO> getGoodsByUserGoodsClass(Long userGoodsClassId) throws NullPointerException, EntityNotFoundException;
}
