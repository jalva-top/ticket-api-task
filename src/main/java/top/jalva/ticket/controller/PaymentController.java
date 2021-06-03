package top.jalva.ticket.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.jalva.ticket.controller.dto.IdDto;
import top.jalva.ticket.controller.dto.IdTicketResponse;
import top.jalva.ticket.controller.dto.StatusDto;
import top.jalva.ticket.controller.dto.StatusTicketResponse;
import top.jalva.ticket.model.TicketOrder;
import top.jalva.ticket.service.TicketOrderPaymentService;

@RestController
@RequestMapping("payment-api/v1")
public class PaymentController {

	TicketOrderPaymentService service;
	ModelMapper mapper;

	@Autowired	
	public PaymentController(TicketOrderPaymentService service, ModelMapper mapper) {
		super();
		this.service = service;
		this.mapper = mapper;
	}

	@GetMapping("/ticket-to-pay")
	public StatusTicketResponse getOldestProcessingTicketOrder() {
		Optional<TicketOrder> ticketToPay = service.findOldestProcessingTicketOrder();
		return createTicketToPayResponse(ticketToPay);
	}

	private StatusTicketResponse createTicketToPayResponse(Optional<TicketOrder> ticket) {
		if(ticket.isPresent()) {
			return new StatusTicketResponse(mapper.map(ticket.get(), StatusDto.class));
		} else {
			return new StatusTicketResponse(null, "No tickets found for payment");
		}
	}

	@PostMapping("/tickets/{id}/payment")
	public IdTicketResponse pay(@PathVariable long id) {
		TicketOrder ticketOrder = service.pay(id);
		return createPaymentResponse(ticketOrder);
	}

	private IdTicketResponse createPaymentResponse(TicketOrder ticketOrder) {
		return new IdTicketResponse(mapper.map(ticketOrder, IdDto.class));
	}
}
