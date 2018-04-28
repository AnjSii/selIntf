package com.tomowork.shop.selIntf.entity;

import java.util.List;

public class StoreClassVO {

	/**
	 * 店铺分类id
	 */
	private Long id;

	/**
	 * 店铺分类名称
	 */
	private String className;

	/**
	 * 店铺分类的父级
	 */
	private StoreClassVO parent;

	/**
	 * 店铺分类的子级
	 */
	private List<StoreClassVO> childs;

	/**
	 * 店铺分类级别
	 */
	private int level;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public StoreClassVO getParent() {
		return parent;
	}

	public void setParent(StoreClassVO parent) {
		this.parent = parent;
	}

	public List<StoreClassVO> getChilds() {
		return childs;
	}

	public void setChilds(List<StoreClassVO> childs) {
		this.childs = childs;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
