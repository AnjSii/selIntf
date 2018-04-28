package com.tomowork.shop.selIntf.entity;

import com.tomowork.shop.api.AreaVO;
import com.tomowork.shop.api.ImageVO;
import com.tomowork.shop.api.StoreVO;

public class StoreSettingVO extends StoreVO {

	/**
	 * 申请的店铺名称
	 */
	private String store_name;

	/**
	 * 申请的店主名称
	 */
	private String store_ower;

	/**
	 * 申请的人的身份证号
	 */
	private String store_ower_card;

	/**
	 * 条幅
	 */
	private ImageVO store_banner;

	/**
	 * 所在地区
	 */
	private AreaVO area;

	/**
	 * 邮编
	 */
	private String store_zip;

	/**
	 * 电话号码
	 */
	private String store_telephone;

	/**
	 * qq
	 */
	private String store_qq;

	/**
	 * seo关键字
	 */
	private String store_seo_keywords;

	/**
	 * seo描述
	 */
	private String store_seo_description;

	/**
	 * 店铺信息
	 */
	private String store_info;

	/**
	 * 申请的店铺的级别
	 */
	private StoreGradeVO grade;

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getStore_ower() {
		return store_ower;
	}

	public void setStore_ower(String store_ower) {
		this.store_ower = store_ower;
	}

	public String getStore_ower_card() {
		return store_ower_card;
	}

	public void setStore_ower_card(String store_ower_card) {
		this.store_ower_card = store_ower_card;
	}

	public ImageVO getStore_banner() {
		return store_banner;
	}

	public void setStore_banner(ImageVO store_banner) {
		this.store_banner = store_banner;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public String getStore_zip() {
		return store_zip;
	}

	public void setStore_zip(String store_zip) {
		this.store_zip = store_zip;
	}

	public String getStore_telephone() {
		return store_telephone;
	}

	public void setStore_telephone(String store_telephone) {
		this.store_telephone = store_telephone;
	}

	public String getStore_qq() {
		return store_qq;
	}

	public void setStore_qq(String store_qq) {
		this.store_qq = store_qq;
	}

	public String getStore_seo_keywords() {
		return store_seo_keywords;
	}

	public void setStore_seo_keywords(String store_seo_keywords) {
		this.store_seo_keywords = store_seo_keywords;
	}

	public String getStore_seo_description() {
		return store_seo_description;
	}

	public void setStore_seo_description(String store_seo_description) {
		this.store_seo_description = store_seo_description;
	}

	public String getStore_info() {
		return store_info;
	}

	public void setStore_info(String store_info) {
		this.store_info = store_info;
	}

	public StoreGradeVO getGrade() {
		return grade;
	}

	public void setGrade(StoreGradeVO grade) {
		this.grade = grade;
	}
}
