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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tomowork.shop.foundation.domain.GoodsBrand;
import com.tomowork.shop.selIntf.entity.GoodsBrandVO;
import com.tomowork.shop.selIntf.service.MobileGoodsBrandService;
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
public class MobileGoodsBrandControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private MobileGoodsBrandController mobileGoodsBrandController;

	@Spy
	private MobileGoodsBrandService mobileGoodsBrandService = mock(MobileGoodsBrandService.class);

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(mobileGoodsBrandController).setControllerAdvice(new RestExceptionHandler())
				.build();
	}

	@Test
	public void test_getGoodsBrandList_httpStatus200() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		List<GoodsBrandVO> goodsBrandVOList = new ArrayList<>();
		GoodsBrandVO goodsBrandVO = new GoodsBrandVO();
		goodsBrandVO.setId(1L);
		goodsBrandVO.setName("品牌");
		goodsBrandVO.setFirst_word("p");
		goodsBrandVO.setRemark("好品牌");
		goodsBrandVOList.add(goodsBrandVO);

		when(mobileGoodsBrandService.getGoodsBrandList("wuxun")).thenReturn(goodsBrandVOList);
		mockMvc.perform(get("/goodsBrands").principal(principal))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].name", is("品牌")))
				.andExpect(jsonPath("$.[0].first_word", is("p")));
	}

	@Test
	public void test_addGoodsBrand_httpStatus201() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		String json = "{\"name\":\"品牌\",\"first_word\":\"p\",\"remark\":\"好品牌\"}";

		GoodsBrand goodsBrand= new GoodsBrand();
		goodsBrand.setId(1L);
		goodsBrand.setName("品牌");
		goodsBrand.setFirst_word("p");
		goodsBrand.setRemark("好品牌");
		when(mobileGoodsBrandService.addGoodsBrand(any(GoodsBrandVO.class), eq("wuxun"))).thenReturn(goodsBrand);
		mockMvc.perform(MockMvcRequestBuilders.post("/goodsBrand").contentType(MediaType.APPLICATION_JSON)
				.content(json).principal(principal))
				.andExpect(status().isCreated());
	}

	@Test
	public void test_alterUserGoods_httpStatus204() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};
		String json = "{\"name\":\"品牌\",\"first_word\":\"p\",\"remark\":\"好品牌\"}";

		doNothing().when(mobileGoodsBrandService).alterGoodsBrand(eq(1L), any(GoodsBrandVO.class), eq("wuxun"));
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.patch("/goodsBrand/1").contentType(MediaType.APPLICATION_JSON)
				.content(json).principal(principal));

		verify(mobileGoodsBrandService).alterGoodsBrand(eq(1L), any(GoodsBrandVO.class), eq("wuxun"));
		actions.andExpect(status().isNoContent());
	}

	@Test
	public void test_delUserGoods() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "xuxun";
			}
		};
		doNothing().when(mobileGoodsBrandService).delGoodsBrand(1L, "xuxun");
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/goodsBrand/1").principal(principal));

		verify(mobileGoodsBrandService).delGoodsBrand(1L, "xuxun");
		actions.andExpect(status().isNoContent());
	}
}
