package top.jalva.ticket.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.jalva.ticket.exception.ApiException;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonPropertyOrder({ "ticket", "info", "error", "errorCode" })
public class IdTicketResponse extends ApiResponse {
	IdDto ticket;
	
	public IdTicketResponse(IdDto ticket) {
		this.ticket = ticket;
	}

	public IdTicketResponse(IdDto ticket, String info) {
		this.ticket = ticket;
		this.info = info;
	}
	
    public IdTicketResponse(ApiException e) {
    	error = e.getMessage();
    	errorCode = e.getErrorCode().orElse(null);
    }
    
    public boolean hasTicket() {
    	return ticket != null;
    }
}
