package com.tomowork.shop.selIntf.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.tomowork.shop.foundation.domain.GoodsCart;
import com.tomowork.shop.foundation.domain.GoodsSpecProperty;
import com.tomowork.shop.foundation.domain.OrderFormItem;
import com.tomowork.shop.foundation.domain.Sku;
import com.tomowork.shop.foundation.domain.Store;
import com.tomowork.shop.foundation.domain.SysConfig;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.GoodsCartService;
import com.tomowork.shop.foundation.service.GoodsService;
import com.tomowork.shop.foundation.service.OrderFormItemService;
import com.tomowork.shop.foundation.service.SysConfigService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.GoodsInWarehouseService;
import com.tomowork.shop.solr.SolrUtils;
import com.tomowork.shop.velocity.tools.view.URIFactory;

@Service
@Transactional
public class GoodsInWarehouseServiceImpl implements GoodsInWarehouseService {

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private UserService userService;

	@Autowired
	@Named("imageUriFactory")
	URIFactory uriFactory;

	@Autowired
	private SysConfigService configService;

	@Autowired
	private GoodsCartService goodsCartService;

	@Autowired
	private OrderFormItemService orderFormItemService;

	@Override
	public List<GoodsVO> getGoodsInWarehouse(String userName) throws NullPointerException, IllegalArgumentException,
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
				params.put("goods_status", 1);
				params.put("store_id", store.getId());
				params.put("delete_status", false);
				List<Goods> goodsList = this.goodsService
						.query("select obj from Goods obj where obj.goods_status = :goods_status and " +
										"obj.goods_store.id = :store_id and obj.deleteStatus = :delete_status order by obj.goods_seller_time desc",
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
	public void goodsOnSale(Long goodsId, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException {
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
		if (goods.getGoods_status() != 1) {
			throw new ViolationException("当前商品不是在仓库中的商品");
		}

		int store_status = store.getStore_status();
		switch (store_status) {
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				goods.setGoods_status(0);
				this.goodsService.update(goods);

				Map<String, Object> map = new HashMap<>();
				map.put("skuSpecDesc", generateSkuSpecDesc(goods.getSkuList()));
				map.put("id", goods.getId().toString());
				map.put("myTitle", goods.getGoods_name());
				map.put("myTitleCopy", goods.getGoods_name());
				map.put("myContent", goods.getGoods_details());
				map.put("markObject", "goods");
				map.put("storePrice", CommUtil.null2Double(goods.getStore_price()));
				map.put("addTime", goods.getAddTime().getTime());
				map.put("goodsSalenum", goods.getGoods_salenum());
				map.put("goodsCollect", goods.getGoods_collect());

				map.put("storeID", goods.getGoods_store().getId());
				map.put("storeName", goods.getGoods_store().getStore_name());
				if (goods.getGc() != null) {
					map.put("goodsClassID", goods.getGc().getId());
					map.put("goodsClassName", goods.getGc().getClassName());
				}
				if (goods.getGoods_main_photo() != null) {
					map.put("mainPhotoPath", goods.getGoods_main_photo().getUrl());
					map.put("mainPhotoPathSmall", goods.getGoods_main_photo().getSmallUrl());
					map.put("ext", goods.getGoods_main_photo().getExt());
				}
				if (goods.getGoods_brand() != null) {
					map.put("goodsBrandName", goods.getGoods_brand().getName());
					map.put("goodsBrandID", goods.getGoods_brand().getId());
				}
				map.put("goods_current_price", CommUtil.null2Double(goods.getGoods_current_price().setScale(2)));
				map.put("goods_price", CommUtil.null2Double(goods.getGoods_price().setScale(2)));
				map.put("group_buy", goods.getGroup_buy());
				map.put("activity_status", goods.getActivity_status());
				map.put("combin_status", goods.getCombin_status());
				map.put("bargain_status", goods.getBargain_status());
				map.put("delivery_status", goods.getDelivery_status());
				map.put("goods_salenum", goods.getGoods_salenum());
				if (goods.getDescription_evaluate() != null)
					map.put("description_evaluate", CommUtil.null2Double(goods.getDescription_evaluate().setScale(2)));
				map.put("evaluatesSize", goods.getEvaluates().size());
				map.put("gradeName", goods.getGoods_store().getGrade().getGradeName());
				SolrUtils.addDoc(map);
				break;
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
	}

	@Override
	public void delGetGoodsInWarehouse(Long goodsId, String userName) throws NullPointerException, IllegalArgumentException, EntityNotFoundException, ViolationException {
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
		if (goods.getGoods_status() != 1) {
			throw new ViolationException("当前商品不是在仓库中的商品");
		}
		int store_status = store.getStore_status();
		switch (store_status) {
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				Map<String, Object> map = new HashMap<>();
				map.put("gid", goods.getId());
				List<GoodsCart> goodCarts = this.goodsCartService
						.query("select obj from GoodsCart obj where obj.goods.id = :gid",
								map, -1, -1);
				List<OrderFormItem> ofi = this.orderFormItemService.query("select obj from OrderFormItem obj where obj.goods.id = :gid",
						map, -1, -1);
				if (goodCarts.size() != 0 || ofi.size() != 0) {
					throw new ViolationException("买家购物车或订单中存在商品，不能删除");
				}

				goods.setDeleteStatus(true);
				this.goodsService.update(goods);
				SolrUtils.deleteById(CommUtil.null2String(goodsId));
				break;
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
	}

	private String generateSkuSpecDesc(List<Sku> skuList) {
		Set<String> skuSpecDesc = new HashSet<String>();
		for (int i = 0; i < skuList.size(); i++) {
			Sku sku = skuList.get(i);
			generateSpecValue(skuSpecDesc, sku.getGsps());
		}
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = skuSpecDesc.iterator(); it.hasNext();) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(',');
			}
		}
		return sb.toString();
	}

	private void generateSpecValue(Set<String> skuSpecDesc, Collection<GoodsSpecProperty> gsps) {
		for (Iterator<GoodsSpecProperty> it = gsps.iterator(); it.hasNext();) {
			GoodsSpecProperty gsp = it.next();
			skuSpecDesc.add(gsp.getValue());
		}
	}
}
