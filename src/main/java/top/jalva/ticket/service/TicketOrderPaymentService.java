package top.jalva.ticket.service;

import java.util.Optional;

import top.jalva.ticket.model.TicketOrder;

public interface TicketOrderPaymentService {

	Optional<TicketOrder> findOldestProcessingTicketOrder();

	TicketOrder pay(long id);

}
