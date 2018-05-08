package com.tomowork.shop.selIntf.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AppealReasnRequestVO {

	@NotNull(message = "申诉原因不能为空")
	@Size(min = 1, message = "请填写申诉原因")
	private String appealReasn;

	public String getAppealReasn() {
		return appealReasn;
	}

	public void setAppealReasn(String appealReasn) {
		this.appealReasn = appealReasn;
	}
}
