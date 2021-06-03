package top.jalva.ticket.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import top.jalva.ticket.controller.constant.Scripts;
import top.jalva.ticket.controller.enums.ApiError;
import top.jalva.ticket.model.enums.PaymentStatus;
import top.jalva.ticket.service.RandomStatusProvider;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class PaymentControllerTest {

	private static final String PAYMENT_API = "/payment-api/v1";
	private static final String ORDER_TO_PAY_URL = PAYMENT_API + "/ticket-to-pay";
	
	@Autowired
	RandomStatusProvider randomStatusProvider;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@Sql(scripts = Scripts.INSERT_PROCESSING_TICKETS, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = Scripts.DELETE_TICKETS, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void orderToPayTest() throws Exception {
		mockMvc.perform(get(ORDER_TO_PAY_URL))//
		.andExpect(jsonPath("$.ticket.id", is(2)))//
		.andExpect(status().isOk()); 
	}
	
	@Test
	@Sql(scripts = Scripts.INSERT_TICKET_ID_2, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = Scripts.DELETE_TICKETS, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void orderToPayTestNoProcessingOrders() throws Exception {
		mockMvc.perform(get(ORDER_TO_PAY_URL))//
		.andExpect(jsonPath("$.ticket").doesNotExist())//
		.andExpect(jsonPath("$.info").isNotEmpty())
		.andExpect(status().isOk()); 
	}
	
	@Test
	@Sql(scripts = Scripts.INSERT_PROCESSING_TICKETS, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = Scripts.DELETE_TICKETS, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void payTest() throws Exception {
		randomStatusProvider.setNextValue(PaymentStatus.PAID);
		mockMvc.perform(post(PAYMENT_API + "/tickets/2/payment"))//
		.andExpect(jsonPath("$.ticket.id", is(2)))//
		.andExpect(status().isOk()); 
	}
	
	@Test
	@Sql(scripts = Scripts.INSERT_PROCESSING_TICKETS, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = Scripts.DELETE_TICKETS, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void payTestWhenError() throws Exception {
		randomStatusProvider.setNextValue(PaymentStatus.ERROR);
		mockMvc.perform(post(PAYMENT_API + "/tickets/2/payment"))//
		.andExpect(jsonPath("$.ticket").doesNotExist())//
		.andExpect(jsonPath("$.errorCode", is(ApiError.PAYMENT_ERROR.getCode())))//
		.andExpect(status().isOk()); 
	}
	
	@Test
	@Sql(scripts = Scripts.INSERT_TICKET_ID_2, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = Scripts.DELETE_TICKETS, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void payTestWhenTicketNotProcessing() throws Exception {
		mockMvc.perform(post(PAYMENT_API + "/tickets/2/payment"))//
		.andExpect(jsonPath("$.ticket").doesNotExist())//
		.andExpect(jsonPath("$.errorCode", is(ApiError.ILLEGAL_ORDER_STATUS.getCode())))//
		.andExpect(status().isOk()); 
	}
}
