package top.jalva.ticket.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import top.jalva.ticket.controller.constant.Scripts;
import top.jalva.ticket.controller.enums.ApiError;
import top.jalva.ticket.model.enums.PaymentStatus;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class TicketOrderControllerTest {
	
	private static final String ORDER_API = "/order-api/v1";
	private static final String NEW_ORDER_URL = ORDER_API + "/ticket";
	
	private static final String VALID_ROUTE = "41";
	private static final String INVALID_ROUTE = "11111r";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void statusTestWhenTicketOrderDoesNotExist() throws Exception {
		mockMvc.perform(get(ORDER_API + "/tickets/1/status"))//
		.andExpect(jsonPath("$.ticket").doesNotExist())
		.andExpect(jsonPath("$.errorCode", is(ApiError.ORDER_DOES_NOT_EXIST.getCode())))
		.andExpect(status().isOk()); 
	}
	
	@Test
	@Sql(scripts= Scripts.INSERT_TICKET_ID_2, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts= Scripts.DELETE_TICKETS, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void statusTest() throws Exception {
		mockMvc.perform(get(ORDER_API + "/tickets/2/status"))//
		.andExpect(jsonPath("$.ticket.paymentStatus", is(PaymentStatus.PAID.name())))
		.andExpect(status().isOk()); 
	}

	@Test
	public void addNewTicketOrderTestWhenRouteDoesNotExist() throws Exception {
		mockMvc.perform(post(NEW_ORDER_URL)
				.contentType(MediaType.APPLICATION_JSON)////
				.content(postContent(getValidDeparture(), INVALID_ROUTE)))//
		.andExpect(jsonPath("$.ticket").doesNotExist())
		.andExpect(jsonPath("$.errorCode", is(ApiError.ROUTE_NOT_FOUND.getCode())))//
		.andExpect(status().isOk()); 
	}

	private LocalDateTime getValidDeparture() {
		return LocalDateTime.now().plusHours(1);
	}
	
	@Test
	public void addNewTicketOrderTestWhenDepartureIsOutdated() throws Exception {
		mockMvc.perform(post(NEW_ORDER_URL)
				.contentType(MediaType.APPLICATION_JSON)////
				.content(postContent(getOutdatedDeparture(), VALID_ROUTE)))//
		.andExpect(jsonPath("$.ticket").doesNotExist())
		.andExpect(jsonPath("$.errorCode", is(ApiError.DEPARTURE_IS_OUTDATED.getCode())))//
		.andExpect(status().isOk()); 
	}

	private LocalDateTime getOutdatedDeparture() {
		return LocalDateTime.now().minusMinutes(1);
	}
	
	@Test
	@Sql(scripts = Scripts.ALTER_AI_TO_5, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = Scripts.DELETE_TICKETS, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void addNewTicketOrderTest() throws Exception {
		mockMvc.perform(post(NEW_ORDER_URL)
				.contentType(MediaType.APPLICATION_JSON)////
				.content(postContent(getValidDeparture(), VALID_ROUTE)))//
		.andExpect(jsonPath("$.ticket.id", is(5)))//
		.andExpect(status().isOk()); 
	}

	String postContent(LocalDateTime departureDateTime, String routeNumber) {
		return "{\n"//
				+ "\"departureDateTime\": \"" + departureDateTime + "\",\n"
				+ "\"routeNumber\": \"" +  routeNumber + "\"\n"//
				+ "}";
	}
}