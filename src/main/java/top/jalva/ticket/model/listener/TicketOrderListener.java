package top.jalva.ticket.model.listener;

import javax.persistence.PrePersist;

import top.jalva.ticket.model.TicketOrder;
import top.jalva.ticket.model.enums.PaymentStatus;

public class TicketOrderListener {
		
		@PrePersist
		public void prePersist(TicketOrder entity) {
			entity.setPaymentStatus(PaymentStatus.PROCESSING);
		}
}
