package com.tomowork.shop.selIntf.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.api.StoreVO;
import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.foundation.domain.Goods;
import com.tomowork.shop.foundation.domain.Store;
import com.tomowork.shop.foundation.domain.SysConfig;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.GoodsService;
import com.tomowork.shop.foundation.service.SysConfigService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.MobileGoodsSoldService;
import com.tomowork.shop.solr.SolrUtils;
import com.tomowork.shop.velocity.tools.view.URIFactory;

@Service
@Transactional
public class MobileGoodsSoldServiceImpl implements MobileGoodsSoldService {

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private UserService userService;

	@Autowired
	@Named("imageUriFactory")
	URIFactory uriFactory;

	@Autowired
	private SysConfigService configService;

	@Override
	public List<GoodsVO> getGoodsSold(String userName) throws NullPointerException, IllegalArgumentException,
			EntityNotFoundException, ViolationException {

		if (userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		Store store = user.getStore();
		if (store == null) {
			throw new EntityNotFoundException("您还没有申请店铺");
		}
		List<GoodsVO> goodsVOList = new ArrayList<>();
		int store_status = store.getStore_status();
		switch (store_status) {
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				HashMap<String, Object> params = new HashMap<>();
				params.put("goods_status", 0);
				params.put("store_id", store.getId());
				List<Goods> goodsList = this.goodsService
						.query("select obj from Goods obj where obj.goods_status = :goods_status and obj.goods_store.id = :store_id order by obj.addTime desc",
								params, -1, -1);

				for (Goods goods : goodsList) {
					GoodsVO goodsVO = new GoodsVO();
					StoreVO storeVO = new StoreVO();
					storeVO.setId(goods.getGoods_store().getId());
					storeVO.setName(goods.getGoods_store().getStore_name());
					goodsVO.setStore(storeVO);
					goodsVO.setId(goods.getId());
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
				break;
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
		return goodsVOList;
	}

	@Override
	public void putInWarehouse(Long goodsId, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException {
		if (userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		User user = this.userService.getUserByUsername(userName);
		Goods goods = this.goodsService.getObjById(goodsId);
		Store store = user.getStore();
		if (store == null) {
			throw new EntityNotFoundException("您还没有申请店铺");
		}
		if (goods == null) {
			throw new EntityNotFoundException("没有查询到商品");
		}
		if (!goods.getGoods_store().getId().equals(store.getId())) {
			throw new EntityNotFoundException("当前商品不符合");
		}
		if (goods.getGoods_status() != 0) {
			throw new ViolationException("当前商品不是正在出售中的商品");
		}

		int store_status = store.getStore_status();
		switch (store_status) {
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				goods.setGoods_status(1);
				this.goodsService.update(goods);
				SolrUtils.deleteById(CommUtil.null2String(goods.getId()));
				break;
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
	}
}
