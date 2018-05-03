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

import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.selIntf.entity.StoreGradeVO;
import com.tomowork.shop.selIntf.service.MobileStoreGradeService;
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
public class StoreGradeControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private StoreGradeController storeGradeController;

	@Spy
	private MobileStoreGradeService mobileStoreGradeService = mock(MobileStoreGradeService.class);

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(storeGradeController).setControllerAdvice(new RestExceptionHandler())
				.build();
	}

	@Test
	public void test_getsSoreGrade_httpStatus200() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		List<StoreGradeVO> storeGradeVOList = new ArrayList<>();
		StoreGradeVO storeGrade = new StoreGradeVO();
		storeGrade.setId(1L);
		storeGrade.setGradeLevel(1);
		storeGrade.setGradeName("免费开店");
		storeGrade.setPrice("200");
		storeGradeVOList.add(storeGrade);

		when(mobileStoreGradeService.getStoreGradeList("wuxun")).thenReturn(storeGradeVOList);
		mockMvc.perform(get("/storeGradnes").principal(principal))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].gradeName", is("免费开店")))
				.andExpect(jsonPath("$.[0].price", is("200")))
				.andExpect(jsonPath("$.[0].gradeLevel", is(1)));
	}

	@Test
	public void test_applyStoreGradnes_http204() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		doNothing().when(mobileStoreGradeService).applyGrade(eq(CommUtil.null2Long(1)), eq("wuxun"));
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/storeGrade")
				.param("storeGradeId", "1")
				.principal(principal));

		verify(mobileStoreGradeService).applyGrade(eq(CommUtil.null2Long(1)), eq("wuxun"));
		actions.andExpect(status().isNoContent());
	}
}
