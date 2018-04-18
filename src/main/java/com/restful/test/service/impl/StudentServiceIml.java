package com.restful.test.service.impl;

import com.restful.test.entity.StudentVO;
import com.restful.test.service.StudentService;
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
