package com.tomowork.shop.selIntf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomowork.shop.foundation.domain.Store;
import com.tomowork.shop.foundation.domain.StoreGrade;
import com.tomowork.shop.foundation.domain.StoreGradeLog;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.StoreGradeLogService;
import com.tomowork.shop.foundation.service.StoreGradeService;
import com.tomowork.shop.foundation.service.StoreService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.entity.StoreGradeVO;
import com.tomowork.shop.selIntf.exception.EntityNotFoundException;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.MobileStoreGradeService;

@Service
@Transactional
public class MobileStoreGradeServiceImpl implements MobileStoreGradeService {

	@Autowired
	private UserService userService;

	@Autowired
	private StoreGradeService storeGradeService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreGradeLogService storeGradeLogService;

	@Override
	public List<StoreGradeVO> getStoreGradeList(String userName) throws ViolationException {
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

		List<StoreGradeVO> storeGradeVOList = new ArrayList<>();
		int store_status = store.getStore_status();
		switch (store_status) {
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				if (store.getUpdate_grade() == null) {
					List<StoreGrade> storeGradeList = this.storeGradeService.query(
							"select obj from StoreGrade obj order by obj.sequence asc",
							null, -1, -1);
					for (StoreGrade storeGrade: storeGradeList) {
						StoreGradeVO storeGradeVO = new StoreGradeVO();
						storeGradeVO.setId(storeGrade.getId());
						storeGradeVO.setPrice(storeGrade.getPrice());
						storeGradeVO.setGradeLevel(storeGrade.getGradeLevel());
						storeGradeVO.setGradeName(storeGrade.getGradeName());
						storeGradeVOList.add(storeGradeVO);
					}
				} else {
					throw new ViolationException("您的店铺升级正在审核中");
				}
				break;
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
		return storeGradeVOList;
	}

	@Override
	public Long applyGrade(Long storeGradeId, String userName) throws NullPointerException, IllegalArgumentException, ViolationException {
		if (storeGradeId == null || userName == null) {
			throw new NullPointerException();
		}
		if (userName.isEmpty()) {
			throw new IllegalArgumentException();
		}

		StoreGrade storeGrade = this.storeGradeService.getObjById(storeGradeId);
		if (storeGrade == null) {
			throw new EntityNotFoundException("没有找到店铺等级");
		}
		Store store = this.userService.getUserByUsername(userName).getStore();
		if (store == null) {
			throw new EntityNotFoundException("您还没开通店铺");
		}
		if (store.getGrade().getId().equals(storeGradeId)) {
			throw new ViolationException("申请等级与当前等级相同");
		}

		int store_status = store.getStore_status();
		switch (store_status) {
			case 1:
				throw new ViolationException("您的店铺正在审核中");
			case 2:
				if (store.getUpdate_grade() == null) {
					store.setUpdate_grade(storeGrade);
					this.storeService.update(store);
					StoreGradeLog grade_log = new StoreGradeLog();
					grade_log.setAddTime(new Date());
					grade_log.setStore(store);
					this.storeGradeLogService.save(grade_log);
				} else {
					throw new ViolationException("您的店铺升级正在审核中");
				}
				break;
			case 3:
				throw new ViolationException("您的店铺已经被关闭");
			case 4:
				throw new ViolationException("您的店铺已经过期关闭");
			case -1:
				throw new ViolationException("您的店铺审核被拒绝");
		}
		return store.getId();
	}
}
