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

import com.tomowork.shop.selIntf.entity.StoreSettingVO;
import com.tomowork.shop.selIntf.service.StoreSettinngService;
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
public class StoreSettingControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private StoreSettingController storeSettingController;

	@Spy
	private StoreSettinngService storeSettinngService = mock(StoreSettinngService.class);

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(storeSettingController).setControllerAdvice(new RestExceptionHandler())
				.build();
	}

	@Test
	public void test_getStoreSetting_httpStatus200() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		StoreSettingVO storeSettingVO = new StoreSettingVO();
		storeSettingVO.setId(1L);
		storeSettingVO.setName("张三");
		storeSettingVO.setAddress("海南省海口市滨海大道财富广场");
		storeSettingVO.setStore_name("张三的店铺");
		storeSettingVO.setStore_ower("张三");
		storeSettingVO.setStore_ower_card("460102199311051512");

		when(storeSettinngService.getStoreSeting("wuxun")).thenReturn(storeSettingVO);
		mockMvc.perform(get("/store/setting").principal(principal))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("张三")))
				.andExpect(jsonPath("$.store_name", is("张三的店铺")))
				.andExpect(jsonPath("$.store_ower", is("张三")))
				.andExpect(jsonPath("$.store_ower_card", is("460102199311051512")))
				.andExpect(jsonPath("$.address", is("海南省海口市滨海大道财富广场")));

	}

	@Test
	public void test_alterStoreSetting_httpStatus204() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		String json = "{\"address\": \"海南省海口市滨海大道财富广场1\",\"area\": {\"id\": 4521986},\"store_zip\": \"570203\"," +
				"\"store_telephone\": \"13846522185\",\"store_qq\": \"93972087\",\"store_seo_keywords\": \"hicailiao,采料\"," +
				"\"store_seo_description\": \"hicailiao,shop\"}";

		doNothing().when(storeSettinngService).alterStoreSetting(any(StoreSettingVO.class), eq("wuxun"));
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.patch("/store/setting").contentType(MediaType.APPLICATION_JSON)
				.content(json).principal(principal));

		verify(storeSettinngService).alterStoreSetting(any(StoreSettingVO.class), eq("wuxun"));
		actions.andExpect(status().isNoContent());
	}

	@Test
	public void test_storeImages_httpStatus204() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		final String store_logo_fileName = "store_logo";
		final byte[] store_logo_content = "This is a store_logo".getBytes();
		final String store_banner_fileName = "store_banner";
		final byte[] store_banner_content = "This is a store_banner".getBytes();

		MockMultipartFile store_logo_file = new MockMultipartFile("store_logo", store_logo_fileName, "application/x-jpg", store_logo_content);
		MockMultipartFile store_banner_file = new MockMultipartFile("store_banner", store_banner_fileName, "application/x-jpg", store_banner_content);

		doNothing().when(storeSettinngService).storeSettinngImages(eq("wuxun"), any(MultipartFile.class), any(MultipartFile.class));
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/store/storeSettinngImages")
				.file(store_logo_file)
				.file(store_banner_file)
				.principal(principal));

		verify(storeSettinngService).storeSettinngImages(eq("wuxun"), any(MultipartFile.class), any(MultipartFile.class));
		actions.andExpect(status().isNoContent());
	}
}
