package com.tomowork.shop.selIntf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomowork.shop.foundation.domain.MobileVerifyCode;
import com.tomowork.shop.foundation.service.MobileVerifyCodeService;
import com.tomowork.shop.selIntf.service.SmsSendRateLimitService;

/**
 * @author kuangxiang
 */
@Service
@Transactional
public class SmsSendRateLimitServiceImpl implements SmsSendRateLimitService {

	@Autowired
	private MobileVerifyCodeService mobileverifycodeService;

	@Override
	public boolean checkRateLimit(String mobile) {
		MobileVerifyCode mvc = null;
		Map<String, Object> params = new HashMap<>();
		params.put("mobile", mobile);
		List<MobileVerifyCode> mvcs = this.mobileverifycodeService
				.query("select obj from MobileVerifyCode obj where obj.mobile=:mobile order by obj.addTime desc",
						params, -1, -1);
		if (mvcs.size() > 0) {
			mvc = mvcs.get(0);
		}
		boolean ret = false;
		if (mvc != null) {
			Date add_time = mvc.getAddTime();
			Date now_time = new Date();
			if (now_time.getTime() - add_time.getTime() < 60000) {
				ret = true;
			}
		}
		return ret;
	}

}
