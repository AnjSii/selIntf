package com.tomowork.shop.selIntf.controller;

import com.tomowork.shop.selIntf.entity.StudentVO;
import com.tomowork.shop.selIntf.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;

	@RequestMapping(value = "/student", method = RequestMethod.GET)
	public ResponseEntity<Object> getStudent() {
			StudentVO student = this.studentService.getStudent();
			return new ResponseEntity<Object>(student, HttpStatus.OK);
	}
}
