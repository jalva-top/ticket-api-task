package top.jalva.ticket.controller.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketOrderRequestDto {
	
	LocalDateTime departureDateTime;
	String routeNumber;
}
