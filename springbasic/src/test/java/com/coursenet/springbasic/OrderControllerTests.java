package com.coursenet.springbasic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.coursenet.springbasic.controller.OrderController;
import com.coursenet.springbasic.dto.OrderRequestDTO;
import com.coursenet.springbasic.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@MockBean
	private OrderService orderService;
	
	@Test
	void test_PostOrderControllerSuccess() throws Exception {
		//Prinsip AAA
		//Arrange (Deklarasi Variable dan Membuat Mocking)
		OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
		orderRequestDTO.setGoodsName("Charger Laptop");
		orderRequestDTO.setReceiverName("Haris");
		orderRequestDTO.setReceiverAddress("Tangerang");
		
		//Act (Menjalankan Component yang akan di test)
		mockMvc.perform(
					post("/orders")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(orderRequestDTO))
				)
		
		//Assert (Membandingkan Hasil)
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andReturn();
	}
}
