package top.jalva.ticket.controller.enums;

public enum ApiError {
	
	ROUTE_NOT_FOUND(1, "Invalid route"),
	DEPARTURE_IS_OUTDATED(2, "Departure time is over"),
	ORDER_DOES_NOT_EXIST(3, "Ticket order does not exist. Invalid id"),
	EMPTY_REQUEST_PARAMETER(4, "Request parameter is necessary but empty"),
	ILLEGAL_ORDER_STATUS(5, "Illegal ticket payment status"), 
	PAYMENT_ERROR(6, "Ticket order payment ended up with status [ERROR]");
	
	int code;
	String message;
	
	private ApiError(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public String getMessage(String postfix) {
		return message + " (" + postfix.trim() + ")";
	}
}
