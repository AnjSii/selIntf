package com.tomowork.shop.selIntf.entity;

import java.util.List;

public class UserGoodsClassVO {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 分类名称
	 */
	private String className;

	/**
	 * 是否显示，1为显示
	 */
	private Boolean display;

	/**
	 * 排序
	 */
	private Integer sequence;

	/**
	 * 上级分类
	 */
	private UserGoodsClassVO parent;

	/**
	 * 子分类
	 */
	private List<UserGoodsClassVO> childs;

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

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public UserGoodsClassVO getParent() {
		return parent;
	}

	public void setParent(UserGoodsClassVO parent) {
		this.parent = parent;
	}

	public List<UserGoodsClassVO> getChilds() {
		return childs;
	}

	public void setChilds(List<UserGoodsClassVO> childs) {
		this.childs = childs;
	}
}
