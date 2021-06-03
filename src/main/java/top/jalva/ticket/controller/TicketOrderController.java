package top.jalva.ticket.controller;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import top.jalva.ticket.controller.dto.ApiResponse;
import top.jalva.ticket.controller.dto.IdDto;
import top.jalva.ticket.controller.dto.IdTicketResponse;
import top.jalva.ticket.controller.dto.StatusDto;
import top.jalva.ticket.controller.dto.StatusTicketResponse;
import top.jalva.ticket.controller.dto.TicketOrderRequestDto;
import top.jalva.ticket.model.TicketOrder;
import top.jalva.ticket.service.TicketOrderService;

@RestController
@RequestMapping("order-api/v1")
public class TicketOrderController {

	Logger log = LoggerFactory.getLogger(getClass());

	TicketOrderService service;
	ModelMapper mapper;

	@Autowired
	public TicketOrderController(TicketOrderService service, ModelMapper mapper) {
		super();
		this.service = service;
		this.mapper = mapper;
	}

	@PostMapping(path = "/ticket")
	public @ResponseBody ApiResponse newTicketOrder(@RequestBody TicketOrderRequestDto payment) {
		TicketOrder newTicketOrder = service.add(payment.getRouteNumber(), payment.getDepartureDateTime());
		log.info("New ticket order (id={}) is added successfully", newTicketOrder.getId());

		return createNewTicketResponse(newTicketOrder);
	}

	private IdTicketResponse createNewTicketResponse(TicketOrder newTicketOrder) {
		return new IdTicketResponse(mapper.map(newTicketOrder, IdDto.class));
	}

	@GetMapping("/tickets/{id}/status")
	public @ResponseBody StatusTicketResponse getPaymentStatus(@PathVariable long id) {
		return createTicketStatusResponse(service.getById(id));
	}
	
	private StatusTicketResponse createTicketStatusResponse(TicketOrder ticketOrder) {
		return new StatusTicketResponse(mapper.map(ticketOrder, StatusDto.class));
	}
}
