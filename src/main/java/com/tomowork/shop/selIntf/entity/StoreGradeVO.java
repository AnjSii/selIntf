package com.tomowork.shop.selIntf.entity;

public class StoreGradeVO {

	/**
	 * 店铺等级ID
	 */
	private Long id;

	/**
	 * 店铺等级名称
	 */
	private String gradeName;

	/**
	 * 店铺等级价格
	 */
	private String price;

	/**
	 * 店铺等级级别
	 */
	private int gradeLevel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(int gradeLevel) {
		this.gradeLevel = gradeLevel;
	}
}
