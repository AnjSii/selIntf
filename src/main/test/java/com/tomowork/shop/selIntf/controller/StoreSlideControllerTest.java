package com.tomowork.shop.selIntf.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.selIntf.entity.StoreGradeVO;
import com.tomowork.shop.selIntf.service.MobileStoreGradeService;
import com.tomowork.shop.selIntf.service.MoblieStoreSlideService;
import com.tomowork.shop.selIntf.util.RestExceptionHandler;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author wuxun
 */
public class StoreSlideControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private StoreSlideController storeSlideController;

	@Spy
	private MoblieStoreSlideService moblieStoreSlideService = mock(MoblieStoreSlideService.class);

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(storeSlideController).setControllerAdvice(new RestExceptionHandler())
				.build();
	}

	@Test
	public void test_storeSlide_httpStatus204() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		final String slideFile_fileName = "slideFile";
		final byte[] slideFile_content = "This is a slideFile".getBytes();

		MockMultipartFile slideFile_file = new MockMultipartFile("slideFile", slideFile_fileName,
				"application/x-jpg", slideFile_content);

		doNothing().when(moblieStoreSlideService).addStroeSlide(eq(2), any(MultipartFile.class),
				eq("http://www.hicailiao.com"), eq("wuxun"));
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/storeSlide")
				.file(slideFile_file)
				.param("slideIndex", "2")
				.param("url", "http://www.hicailiao.com")
				.principal(principal));

		verify(moblieStoreSlideService).addStroeSlide(eq(2), any(MultipartFile.class),
				eq("http://www.hicailiao.com"), eq("wuxun"));
		actions.andExpect(status().isNoContent());
	}
}
