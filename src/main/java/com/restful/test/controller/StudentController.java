package com.restful.test.controller;

import com.restful.test.entity.StudentVO;
import com.restful.test.service.StudentService;
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
