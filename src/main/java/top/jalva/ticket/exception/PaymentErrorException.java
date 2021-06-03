package top.jalva.ticket.exception;

public class PaymentErrorException extends ApiException {

	private static final long serialVersionUID = -911750093171696190L;

	public PaymentErrorException(Integer errorCode, String message) {
		super(errorCode, message);
	}

}
