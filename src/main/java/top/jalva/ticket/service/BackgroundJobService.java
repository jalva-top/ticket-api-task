package top.jalva.ticket.service;

public interface BackgroundJobService {

	void handleTicketOrder();

	void setActive(boolean isActive);
}
