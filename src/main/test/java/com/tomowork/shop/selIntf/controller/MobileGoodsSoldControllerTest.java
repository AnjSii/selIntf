package com.tomowork.shop.selIntf.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.selIntf.service.MobileGoodsSoldService;
import com.tomowork.shop.selIntf.util.RestExceptionHandler;

import static org.hamcrest.core.Is.is;
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
public class MobileGoodsSoldControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private MobileGoodsSoldController mobileGoodsSoldController;

	@Spy
	private MobileGoodsSoldService mobileGoodsSoldService = mock(MobileGoodsSoldService.class);

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(mobileGoodsSoldController).setControllerAdvice(new RestExceptionHandler())
				.build();
	}

	@Test
	public void test_getGoodsSoldList_httpStatus200() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		List<GoodsVO> goodsVOList = new ArrayList<>();
		GoodsVO goodsVO = new GoodsVO();
		goodsVO.setId(1L);
		goodsVO.setName("商品");
		goodsVO.setSales(21L);
		goodsVO.setInventory(2333);
		goodsVO.setPrice(new BigDecimal(10));
		goodsVOList.add(goodsVO);

		when(mobileGoodsSoldService.getGoodsSold("wuxun")).thenReturn(goodsVOList);
		mockMvc.perform(get("/goodsSold").principal(principal))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].name", is("商品")))
				.andExpect(jsonPath("$.[0].sales", is(21)))
				.andExpect(jsonPath("$.[0].inventory", is(2333)))
				.andExpect(jsonPath("$.[0].price", is(10)));
	}

	@Test
	public void test_putInWarehouse_httpStatus() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "xuxun";
			}
		};

		doNothing().when(mobileGoodsSoldService).putInWarehouse(eq(1L), eq("wuxun"));
		mockMvc.perform(MockMvcRequestBuilders.put("/goodsSold/1").principal(principal))
		.andExpect(status().isNoContent());
	}
}
