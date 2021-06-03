package top.jalva.ticket.model.enums;

public enum PaymentStatus {
	
	PROCESSING, PAID, ERROR;
	
	public boolean isEditable() {
		return this == PROCESSING;
	}
}
