package com.tomowork.shop.selIntf.entity;

import com.tomowork.shop.api.ImageVO;

public class GoodsBrandVO {

	/**
	 * 品牌id
	 */
	private Long id;

	/**
	 * 品牌名称
	 */
	private String name;

	/**
	 * 排序
	 */
	private int sequence;

	/**
	 * 品牌logo
	 */
	private String brandLogo_url;

	/**
	 * 品牌首字母
	 */
	private String first_word;

	/**
	 * 审核状态
	 */
	private GodosBrandStatus audit;

	/**
	 * 申请备注
	 */
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getBrandLogo_url() {
		return brandLogo_url;
	}

	public void setBrandLogo_url(String brandLogo_url) {
		this.brandLogo_url = brandLogo_url;
	}

	public String getFirst_word() {
		return first_word;
	}

	public void setFirst_word(String first_word) {
		this.first_word = first_word;
	}

	public GodosBrandStatus getAudit() {
		return audit;
	}

	public void setAudit(GodosBrandStatus audit) {
		this.audit = audit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 审核状态
	 */
	public enum GodosBrandStatus {
		/**
		 * 审核失败
		 */
		audited_fail,

		/**
		 * 审核成功
		 */
		audited_success,

		/**
		 * 新的审核申请
		 */
		newAudited
	}
}
