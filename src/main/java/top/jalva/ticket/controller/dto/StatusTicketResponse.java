package top.jalva.ticket.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonPropertyOrder({ "ticket", "info", "error", "errorCode" })
public class StatusTicketResponse extends ApiResponse {
	
	StatusDto ticket;
	
	public StatusTicketResponse(StatusDto ticket) {
		super();
		this.ticket = ticket;
	}

	public StatusTicketResponse(StatusDto ticket, String info) {
		super();
		this.ticket = ticket;
		this.info = info;
	}
	
    public boolean hasTicket() {
    	return ticket != null;
    }
}
