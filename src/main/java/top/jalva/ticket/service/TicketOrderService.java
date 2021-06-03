package top.jalva.ticket.service;

import java.time.LocalDateTime;

import top.jalva.ticket.model.TicketOrder;

public interface TicketOrderService {
	TicketOrder add(String routeNumber, LocalDateTime departure);
	TicketOrder getById(long id);
}
