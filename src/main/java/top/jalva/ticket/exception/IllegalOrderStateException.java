package top.jalva.ticket.exception;

public class IllegalOrderStateException extends ApiException {

	private static final long serialVersionUID = -695748437071841326L;

	public IllegalOrderStateException(Integer errorCode, String message) {
		super(errorCode, message);
	}

}
