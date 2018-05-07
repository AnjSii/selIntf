package com.tomowork.shop.selIntf.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.foundation.domain.GoodsBrand;
import com.tomowork.shop.selIntf.entity.GoodsBrandVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;

public interface MobileGoodsBrandService {

	/**
	 * 获取店铺品牌
	 * @param userName 用户名
	 * @return 店铺品牌列表
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 */
	List<GoodsBrandVO> getGoodsBrandList(String userName) throws NullPointerException, IllegalArgumentException;


	/**
	 * 添加店铺品牌
	 * @param goodsBrandVO 店铺品牌类
	 * @param userName 用户名
	 * @return 店铺品牌
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 */
	GoodsBrand addGoodsBrand(GoodsBrandVO goodsBrandVO, String userName) throws NullPointerException, IllegalArgumentException;

	/**
	 * 添加店铺品牌logo
	 * @param brandLogo 品牌标志
	 * @param userName 用户名
	 * @return 店铺品牌
	 * @throws NullPointerException 用户名为空 品牌名称为空 品牌首字母为空 备注为空 品牌标志为空
	 * @throws IllegalArgumentException 用户名为空字符串 品牌名称为空字符串 品牌首字母为空字符串 备注为空字符串为 品牌标志文件名为空字符串
	 * @throws EntityNotFoundException 未找到品牌
	 * @throws ViolationException 图片格式不正确 品牌与当前用户不符
	 */
	GoodsBrand addGoodsBrandLogo(Long goodsBrandId, MultipartFile brandLogo, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException;

	/**
	 * 修改店铺品牌
	 * @param goodsBrandId 品牌的id
	 * @param goodsBrandVO 品牌类
	 * @param userName 用户名
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 使用goodsBrandId没有查询到所要修改的分类 使用goodsBrandId查出来的分类与当前登陆用户不匹配
	 */
	void alterGoodsBrand(Long goodsBrandId, GoodsBrandVO goodsBrandVO, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException;

	/**
	 * 店铺品牌删除
	 * @param goodsBrandId 品牌的id
	 * @param userName 用户名
	 * @throws NullPointerException 用户名为空
	 * @throws IllegalArgumentException 用户名为空字符串
	 * @throws EntityNotFoundException 使用goodsBrandId没有查询到所要修改的分类 使用goodsBrandId查出来的分类与当前登陆用户不匹配
	 */
	void delGoodsBrand(Long goodsBrandId, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException;
}
