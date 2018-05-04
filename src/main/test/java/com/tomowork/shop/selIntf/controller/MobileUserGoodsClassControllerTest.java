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

import com.tomowork.shop.api.AddressVO;
import com.tomowork.shop.api.GoodsVO;
import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.foundation.domain.Goods;
import com.tomowork.shop.foundation.domain.UserGoodsClass;
import com.tomowork.shop.selIntf.entity.StoreApplicationVO;
import com.tomowork.shop.selIntf.entity.UserGoodsClassVO;
import com.tomowork.shop.selIntf.service.MobileUserGoodsClassService;
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
public class MobileUserGoodsClassControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private MobileUserGoodsClassController mobileUserGoodsClassController;

	@Spy
	private MobileUserGoodsClassService mobileUserGoodsClassService = mock(MobileUserGoodsClassService.class);

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(mobileUserGoodsClassController).setControllerAdvice(new RestExceptionHandler())
				.build();
	}

	@Test
	public void test_getUserGoodsList_httpStatus200() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		List<UserGoodsClassVO> userGoodsClassVOList = new ArrayList<>();
		UserGoodsClassVO userGoodsClassVO = new UserGoodsClassVO();
		userGoodsClassVO.setId(1L);
		userGoodsClassVO.setDisplay(true);
		userGoodsClassVO.setClassName("灯具");
		userGoodsClassVOList.add(userGoodsClassVO);

		when(mobileUserGoodsClassService.getUserGoodsClassList("wuxun")).thenReturn(userGoodsClassVOList);
		mockMvc.perform(get("/userGoodsClass").principal(principal))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0]className", is("灯具")))
				.andExpect(jsonPath("$.[0]display", is(true)));
	}

	@Test
	public void test_addUserGoods_httpStatus201() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		String json = "{\"className\": \"测试\",\"sequence\": \"1\",\"display\": true,\"parent\": {\"id\": \"123333333\"}}";

		UserGoodsClass userGoodsClass = new UserGoodsClass();
		userGoodsClass.setId(1L);
		userGoodsClass.setClassName("测试");
		userGoodsClass.setSequence(1);
		userGoodsClass.setLevel(1);
		when(mobileUserGoodsClassService.addUserGoodsClass(any(UserGoodsClassVO.class), eq("wuxun"))).thenReturn(userGoodsClass);
		mockMvc.perform(MockMvcRequestBuilders.post("/userGoodsClass").contentType(MediaType.APPLICATION_JSON)
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
		String json = "{\"className\": \"测试\",\"sequence\": \"1\",\"display\": true,\"parent\": {\"id\": \"123333333\"}}";

		doNothing().when(mobileUserGoodsClassService).alterUserGoodsClass(eq(1L), any(UserGoodsClassVO.class), eq("wuxun"));
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.patch("/userGoodsClass/1").contentType(MediaType.APPLICATION_JSON)
				.content(json).principal(principal));

		verify(mobileUserGoodsClassService).alterUserGoodsClass(eq(1L), any(UserGoodsClassVO.class), eq("wuxun"));
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
		doNothing().when(mobileUserGoodsClassService).delUserGoodsClass(1L, "xuxun");
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/userGoodsClass/1").principal(principal));

		verify(mobileUserGoodsClassService).delUserGoodsClass(1L, "xuxun");
		actions.andExpect(status().isNoContent());
	}

	@Test
	public void test_getGoodsByUserGoodsClass_httpStatus200() throws Exception {

		List<GoodsVO> goodsVOList = new ArrayList<>();
		GoodsVO goodsVO = new GoodsVO();
		goodsVO.setId(1L);
		goodsVO.setName("商品");
		goodsVO.setSales(21L);
		goodsVO.setInventory(2333);
		goodsVO.setPrice(new BigDecimal(10));
		goodsVOList.add(goodsVO);

		when(mobileUserGoodsClassService.getGoodsByUserGoodsClass(eq(1L))).thenReturn(goodsVOList);
		mockMvc.perform(get("/userGoodsClass/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].name", is("商品")))
				.andExpect(jsonPath("$.[0].sales", is(21)))
				.andExpect(jsonPath("$.[0].inventory", is(2333)))
				.andExpect(jsonPath("$.[0].price", is(10)));
	}
}
