package com.tomowork.shop.selIntf.entity;

import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.selIntf.util.validator.Url;

public class StoreSlideRequestVO {

	private MultipartFile slide_one;

	private MultipartFile slide_two;

	private MultipartFile slide_three;

	private MultipartFile slide_four;

	private MultipartFile slide_five;

	@Url
	private String slide_one_url;

	@Url
	private String slide_two_url;

	@Url
	private String slide_three_url;

	@Url
	private String slide_four_url;

	@Url
	private String slide_five_url;

	public MultipartFile getSlide_one() {
		return slide_one;
	}

	public void setSlide_one(MultipartFile slide_one) {
		this.slide_one = slide_one;
	}

	public MultipartFile getSlide_two() {
		return slide_two;
	}

	public void setSlide_two(MultipartFile slide_two) {
		this.slide_two = slide_two;
	}

	public MultipartFile getSlide_three() {
		return slide_three;
	}

	public void setSlide_three(MultipartFile slide_three) {
		this.slide_three = slide_three;
	}

	public MultipartFile getSlide_four() {
		return slide_four;
	}

	public void setSlide_four(MultipartFile slide_four) {
		this.slide_four = slide_four;
	}

	public MultipartFile getSlide_five() {
		return slide_five;
	}

	public void setSlide_five(MultipartFile slide_five) {
		this.slide_five = slide_five;
	}

	public String getSlide_one_url() {
		return slide_one_url;
	}

	public void setSlide_one_url(String slide_one_url) {
		this.slide_one_url = slide_one_url;
	}

	public String getSlide_two_url() {
		return slide_two_url;
	}

	public void setSlide_two_url(String slide_two_url) {
		this.slide_two_url = slide_two_url;
	}

	public String getSlide_three_url() {
		return slide_three_url;
	}

	public void setSlide_three_url(String slide_three_url) {
		this.slide_three_url = slide_three_url;
	}

	public String getSlide_four_url() {
		return slide_four_url;
	}

	public void setSlide_four_url(String slide_four_url) {
		this.slide_four_url = slide_four_url;
	}

	public String getSlide_five_url() {
		return slide_five_url;
	}

	public void setSlide_five_url(String slide_five_url) {
		this.slide_five_url = slide_five_url;
	}
}
