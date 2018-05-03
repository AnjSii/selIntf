package com.tomowork.shop.selIntf.controller;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.selIntf.entity.StoreApplicationVO;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.StoreApplicationService;
import com.tomowork.shop.selIntf.util.RestExceptionHandler;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author wuxun
 */
public class StoreApplicationControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private StoreApplicationController storeApplicationController;

	@Spy
	private StoreApplicationService storeApplicationService = mock(StoreApplicationService.class);

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(storeApplicationController).setControllerAdvice(new RestExceptionHandler())
				.build();
	}

	@Test
	public void test_createStoreApplication_httpStatus201() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		String json = "{\"store_name\": \"大1\",\"store_ower\": \"wuxun\",\"store_ower_card\": \"460102199311051512\"," +
				"\"store_qq\": \"294165407\",\"store_telephone\": \"18308985509\",\"store_zip\": \"570201\"," +
				"\"store_address\": \"海南省海口市\",\"area\":{\"id\": \"4522074\"},\"grade\":{\"id\": \"1\"}," +
				"\"storeClass\":{\"id\": \"1\"}}";

		when(storeApplicationService.createStoreApplication(any(StoreApplicationVO.class), eq("wuxun"))).thenReturn(CommUtil.null2Long(1));
		mockMvc.perform(MockMvcRequestBuilders.post("/storeApplication").contentType(MediaType.APPLICATION_JSON)
				.content(json).principal(principal))
				.andExpect(status().isCreated());
	}

	@Test
	public void test_createStoreApplication_httpStatus422() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		String json = "{\"store_name\": \"大1\",\"store_ower\": \"wuxun\",\"store_ower_card\": \"460102199311051512\"," +
				"\"store_qq\": \"294165407\",\"store_telephone\": \"18308985509\",\"store_zip\": \"570201\"," +
				"\"store_address\": \"海南省海口市\",\"area\":{\"id\": \"4522074\"},\"grade\":{\"id\": \"1\"}," +
				"\"storeClass\":{\"id\": \"1\"}}";

		when(storeApplicationService.createStoreApplication(any(StoreApplicationVO.class), eq("wuxun"))).thenThrow(new ViolationException());
		mockMvc.perform(MockMvcRequestBuilders.post("/storeApplication").contentType(MediaType.APPLICATION_JSON)
				.content(json).principal(principal))
				.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void test_imagesAuthenticate_httpStatus201() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		final String card_front_fileName = "card_front";
		final byte[] card_front_content = "This is a card_front".getBytes();
		final String card_side_fileName = "card_side";
		final byte[] card_side_content = "This is a card_side".getBytes();
		final String card_hand_fileName = "card_hand";
		final byte[] card_hand_content = "This is a card_hand".getBytes();
		final String licebse_file_fileName = "licebse_file";
		final byte[] licebse_file_content = "This is a licebse_file".getBytes();

		MockMultipartFile card_front_file = new MockMultipartFile("card_front", card_front_fileName, "application/x-jpg", card_front_content);
		MockMultipartFile card_side_file = new MockMultipartFile("card_side", card_side_fileName, "application/x-jpg", card_side_content);
		MockMultipartFile card_hand_file = new MockMultipartFile("card_hand", card_hand_fileName, "application/x-jpg", card_hand_content);
		MockMultipartFile licebse_file_file = new MockMultipartFile("icebse_file", licebse_file_fileName, "application/x-jpg", licebse_file_content);

		when(storeApplicationService
				.imagesAuthenticate(eq("wuxun"), any(MultipartFile.class), any(MultipartFile.class), any(MultipartFile.class), any(MultipartFile.class)))
				.thenReturn(CommUtil.null2Long(1));
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/storeApplication/authentication")
				.file(card_front_file)
				.file(card_side_file)
				.file(card_hand_file)
				.file(licebse_file_file)
				.principal(principal))
				.andExpect(status().isCreated());
	}

	@Test
	public void test_getStoreApplication_httpStatus200() throws Exception {
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "wuxun";
			}
		};

		StoreApplicationVO storeApplicationVO = new StoreApplicationVO();
		storeApplicationVO.setId(1L);
		storeApplicationVO.setStore_name("开店申请");
		storeApplicationVO.setStore_ower("张三");
		storeApplicationVO.setStore_ower_card("460102199311051512");
		storeApplicationVO.setStore_qq("294165492");
		storeApplicationVO.setStore_telephone("13876077289");
		storeApplicationVO.setStore_zip("570202");
		storeApplicationVO.setStore_address("海南省海口市滨海大道财富广场");

		when(storeApplicationService.getStoreApplication("wuxun")).thenReturn(storeApplicationVO);
		mockMvc.perform(get("/storeApplication/details").principal(principal))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.store_name", is("开店申请")))
				.andExpect(jsonPath("$.store_ower", is("张三")))
				.andExpect(jsonPath("$.store_ower_card", is("460102199311051512")))
				.andExpect(jsonPath("$.store_qq", is("294165492")))
				.andExpect(jsonPath("$.store_telephone", is("13876077289")))
				.andExpect(jsonPath("$.store_zip", is("570202")))
				.andExpect(jsonPath("$.store_address", is("海南省海口市滨海大道财富广场")));
	}
}
