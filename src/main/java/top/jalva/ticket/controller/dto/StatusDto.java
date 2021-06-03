package top.jalva.ticket.controller.dto;

import lombok.Data;
import top.jalva.ticket.model.enums.PaymentStatus;

@Data
public class StatusDto {
	
	long id;
	PaymentStatus paymentStatus;
}
