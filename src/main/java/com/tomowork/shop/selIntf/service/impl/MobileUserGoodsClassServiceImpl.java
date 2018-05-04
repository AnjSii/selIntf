package com.tomowork.shop.selIntf.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.api.StoreVO;
import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.foundation.domain.Goods;
import com.tomowork.shop.foundation.domain.SysConfig;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.domain.UserGoodsClass;
import com.tomowork.shop.foundation.service.GoodsService;
import com.tomowork.shop.foundation.service.SysConfigService;
import com.tomowork.shop.foundation.service.UserGoodsClassService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.entity.UserGoodsClassVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.MobileUserGoodsClassService;
import com.tomowork.shop.velocity.tools.view.URIFactory;

@Service
@Transactional
public class MobileUserGoodsClassServiceImpl implements MobileUserGoodsClassService{

	@Autowired
	private  UserService userService;

	@Autowired
	private UserGoodsClassService userGoodsClassService;

	@Autowired
	@Named("imageUriFactory")
	URIFactory uriFactory;

	@Autowired
	private SysConfigService configService;

	@Autowired
	private GoodsService goodsService;

	@Override
	public List<UserGoodsClassVO> getUserGoodsClassList(String userName) throws NullPointerException, IllegalArgumentException {

		if (userName == null) {
			throw new NullPointerException();
		}

		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);

		Map<String, Object> param = new HashMap<>();
		param.put("user_id", user.getId());
		List<UserGoodsClass> userGoodsClassList = this.userGoodsClassService
				.query("select obj from UserGoodsClass obj where obj.parent.id=null and obj.user.id=:user_id order by obj.sequence asc",
						param, -1, -1);

		List<UserGoodsClassVO> userGoodsClassVOList = new ArrayList<>();
		for (UserGoodsClass userGoodsClass : userGoodsClassList) {
			UserGoodsClassVO userGoodsClassVO = new UserGoodsClassVO();
			userGoodsClassVO.setId(userGoodsClass.getId());
			userGoodsClassVO.setClassName(userGoodsClass.getClassName());
			userGoodsClassVO.setDisplay(userGoodsClass.isDisplay());
			userGoodsClassVO.setSequence(userGoodsClass.getSequence());

			List<UserGoodsClassVO> userGoodsClassChildsVOList = new ArrayList<>();
			for (UserGoodsClass userGoodsClassChilds: userGoodsClass.getChilds()) {
				UserGoodsClassVO userGoodsClassChildsVO = new UserGoodsClassVO();
				userGoodsClassChildsVO.setId(userGoodsClassChilds.getId());
				userGoodsClassChildsVO.setClassName(userGoodsClassChilds.getClassName());
				userGoodsClassChildsVO.setDisplay(userGoodsClassChilds.isDisplay());
				userGoodsClassChildsVO.setSequence(userGoodsClassChilds.getSequence());
				userGoodsClassChildsVOList.add(userGoodsClassChildsVO);
			}
			userGoodsClassVO.setChilds(userGoodsClassChildsVOList);
			userGoodsClassVOList.add(userGoodsClassVO);
		}
		return userGoodsClassVOList;
	}

	@Override
	public UserGoodsClass addUserGoodsClass(UserGoodsClassVO userGoodsClassVO, String userName) throws NullPointerException,
			IllegalArgumentException, ViolationException {
		if (userName == null || userGoodsClassVO == null) {
			throw new NullPointerException();
		}

		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		UserGoodsClass userGoodsClass = new UserGoodsClass();
		if (userGoodsClassVO.getParent() != null && userGoodsClassVO.getParent().getId() != null) {
			UserGoodsClass userGoodsClassParent = this.userGoodsClassService.getObjById(userGoodsClassVO.getParent().getId());
			if (userGoodsClassParent == null) {
				throw new ViolationException("上级分类没找到");
			}
			userGoodsClass.setParent(userGoodsClassParent);
		}
		User user = this.userService.getUserByUsername(userName);
		userGoodsClass.setClassName(userGoodsClassVO.getClassName());
		userGoodsClass.setDisplay(userGoodsClassVO.getDisplay());
		userGoodsClass.setUser(user);
		userGoodsClass.setAddTime(new Date());
		if (userGoodsClassVO.getSequence() != null) {
			userGoodsClass.setSequence(userGoodsClassVO.getSequence());
		}
		this.userGoodsClassService.save(userGoodsClass);
		return userGoodsClass;
	}

	@Override
	public void alterUserGoodsClass(Long userGoodsClassId, UserGoodsClassVO userGoodsClassVO, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException {
		if (userGoodsClassVO == null || userGoodsClassId == null || userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		UserGoodsClass userGoodsClass = this.userGoodsClassService.getObjById(userGoodsClassId);
		if (userGoodsClass == null) {
			throw new EntityNotFoundException("没有查询到所要修改的分类");
		}
		if (!(userGoodsClass.getUser().getId().equals(user.getId()))) {
			throw new EntityNotFoundException("分类与用户不匹配");
		}

		if (userGoodsClassVO.getParent() != null && userGoodsClassVO.getParent().getId() != null) {
			UserGoodsClass userGoodsClassParent = this.userGoodsClassService.getObjById(userGoodsClassVO.getParent().getId());
			if (userGoodsClassParent == null) {
				throw new ViolationException("上级分类没找到");
			}
			userGoodsClass.setParent(userGoodsClassParent);
		}

		if (userGoodsClassVO.getClassName() != null) {
			userGoodsClass.setClassName(userGoodsClassVO.getClassName());
		}

		if (userGoodsClassVO.getSequence() != null) {
			userGoodsClass.setSequence(userGoodsClassVO.getSequence());
		}

		if (userGoodsClassVO.getDisplay() != null) {
			userGoodsClass.setDisplay(userGoodsClassVO.getDisplay());
		}

		this.userGoodsClassService.update(userGoodsClass);
	}

	@Override
	public void delUserGoodsClass(Long userGoodsClassId, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException {
		if (userGoodsClassId == null || userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		UserGoodsClass userGoodsClass = this.userGoodsClassService.getObjById(userGoodsClassId);
		if (userGoodsClass == null) {
			throw new EntityNotFoundException("没有查询到所要删除的分类");
		}
		if (!(userGoodsClass.getUser().getId().equals(user.getId()))) {
			throw new EntityNotFoundException("分类与用户不匹配");
		}

		if (userGoodsClass.getChilds().size() > 0) {
			throw new ViolationException("不能删除，存在子分类");
		}

		this.userGoodsClassService.delete(userGoodsClassId);
	}

	@Override
	public List<GoodsVO> getGoodsByUserGoodsClass(Long userGoodsClassId) throws NullPointerException, IllegalArgumentException, EntityNotFoundException {
		if (userGoodsClassId == null) {
			throw new NullPointerException();
		}

		UserGoodsClass userGoodsClass = this.userGoodsClassService.getObjById(userGoodsClassId);
		if (userGoodsClass == null) {
			throw new EntityNotFoundException("没有查询到分类");
		}

		Set<Long> id = userGoodsClass.genericIds();
		List<UserGoodsClass> userGoodsClassList = new ArrayList<>();
		for (Long g_id : id) {
			UserGoodsClass temp_ugc = this.userGoodsClassService
					.getObjById(g_id);
			userGoodsClassList.add(temp_ugc);
		}

		HashMap<String, Object> params = new HashMap<>();
		params.put("goods_status", 0);
		params.put("goods_store_id", userGoodsClass.getUser().getStore().getId());
		String query = "select obj from Goods obj where obj.goods_store.id=:goods_store_id and obj.goods_status=:goods_status";
		for (int i = 0; i < userGoodsClassList.size(); i++) {
			if (i == 0) {
				query = query + " and (:ugc" + i + " member of obj.goods_ugcs";
				if (userGoodsClassList.size() == 1) {
					query = query + ")";
				}
			} else if (i == userGoodsClassList.size() - 1) {
				query = query + " or :ugc" + i + " member of obj.goods_ugcs)";
			} else {
				query = query + " or :ugc" + i + " member of obj.goods_ugcs";
			}
			params.put("ugc" + i, userGoodsClassList.get(i));
		}

		List<Goods> goodsList = this.goodsService.query(query, params, -1, -1);

		List<GoodsVO> goodsVOList = new ArrayList<>();
		for (Goods goods : goodsList) {
			GoodsVO goodsVO = new GoodsVO();
			StoreVO storeVO = new StoreVO();
			storeVO.setId(goods.getGoods_store().getId());
			storeVO.setName(goods.getGoods_store().getStore_name());
			goodsVO.setStore(storeVO);
			goodsVO.setId(CommUtil.null2Long(goods.getId()));
			goodsVO.setName(goods.getGoods_name());
			goodsVO.setPrice(goods.getGoods_current_price());
			goodsVO.setSales(CommUtil.null2Long(goods.getGoods_salenum()));
			if (goods.getGoods_main_photo() != null) {
				URI uri = uriFactory.createURI(goods.getGoods_main_photo().getUrl(), null, null);
				goodsVO.setImg_url(uri.toString());
			} else {
				SysConfig config = this.configService.getSysConfig();
				URI uri = uriFactory.createURI(config.getGoodsImage().getUrl(), null, null);
				goodsVO.setImg_url(uri.toString());
			}
			goodsVOList.add(goodsVO);
		}
		return goodsVOList;
	}

}
