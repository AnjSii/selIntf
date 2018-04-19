package com.tomowork.shop.selIntf.service.impl;

import com.tomowork.shop.selIntf.entity.StudentVO;
import com.tomowork.shop.selIntf.service.StudentService;

import org.springframework.stereotype.Service;

@Service
public class StudentServiceIml implements StudentService {

	public StudentVO getStudent () {
		StudentVO studentVO = new StudentVO();
		studentVO.setStudent_id(Long.valueOf(1));
		studentVO.setStudent_name("Jack");
		return studentVO;
	}
}
