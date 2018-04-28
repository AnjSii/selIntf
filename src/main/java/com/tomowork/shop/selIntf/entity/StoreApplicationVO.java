package com.tomowork.shop.selIntf.entity;

import com.tomowork.shop.api.AreaVO;
import com.tomowork.shop.api.ImageVO;

public class StoreApplicationVO {

	/**
	 * 申请记录的id
	 */
	private Long id;

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
	 * 申请的店铺qq
	 */
	private String store_qq;

	/**
	 * 联系电话
	 */
	private String store_telephone;

	/**
	 * 邮编
	 */
	private String store_zip;

	/**
	 * 申请的店铺详细地址
	 */
	private String store_address;


	/**
	 * 申请的店铺所在地区
	 */
	private AreaVO area;

	/**
	 * 申请的店铺的级别
	 */
	private StoreGradeVO grade;

	/**
	 * 申请的店铺的分类
	 */
	private StoreClassVO storeClass;

	/**
	 * 身份证正面
	 */
	private ImageVO card_front;

	/**
	 * 身份证反面
	 */
	private ImageVO card_side;

	/**
	 * 手执身份证
	 */
	private ImageVO card_hand;

	/**
	 * 营业执照
	 */
	private ImageVO licebse_file;

	/**
	 * 审核状态
	 */
	private StoreApplicationStatus status;

	/**
	 * 审核理由
	 */
	private String audit_reason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getStore_qq() {
		return store_qq;
	}

	public void setStore_qq(String store_qq) {
		this.store_qq = store_qq;
	}

	public String getStore_telephone() {
		return store_telephone;
	}

	public void setStore_telephone(String store_telephone) {
		this.store_telephone = store_telephone;
	}

	public String getStore_zip() {
		return store_zip;
	}

	public void setStore_zip(String store_zip) {
		this.store_zip = store_zip;
	}

	public String getStore_address() {
		return store_address;
	}

	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public StoreGradeVO getGrade() {
		return grade;
	}

	public void setGrade(StoreGradeVO grade) {
		this.grade = grade;
	}

	public StoreClassVO getStoreClass() {
		return storeClass;
	}

	public void setStoreClass(StoreClassVO storeClass) {
		this.storeClass = storeClass;
	}

	public ImageVO getCard_front() {
		return card_front;
	}

	public void setCard_front(ImageVO card_front) {
		this.card_front = card_front;
	}

	public ImageVO getCard_side() {
		return card_side;
	}

	public void setCard_side(ImageVO card_side) {
		this.card_side = card_side;
	}

	public ImageVO getCard_hand() {
		return card_hand;
	}

	public void setCard_hand(ImageVO card_hand) {
		this.card_hand = card_hand;
	}

	public ImageVO getLicebse_file() {
		return licebse_file;
	}

	public void setLicebse_file(ImageVO licebse_file) {
		this.licebse_file = licebse_file;
	}

	public StoreApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(StoreApplicationStatus status) {
		this.status = status;
	}

	public String getAudit_reason() {
		return audit_reason;
	}

	public void setAudit_reason(String audit_reason) {
		this.audit_reason = audit_reason;
	}

	/**
	 * 审核状态
	 */
	public enum StoreApplicationStatus {
		/**
		 * 未审核
		 */
		unaudited,

		/**
		 * 已审核
		 */
		audited,

		/**
		 * 新的审核申请
		 */
		newAudited
	}
}
