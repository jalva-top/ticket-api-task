package top.jalva.ticket.exception;

public class ApiEntityNotFoundException extends ApiException {

	private static final long serialVersionUID = 1688306402701055878L;

	public ApiEntityNotFoundException(Integer errorCode, String message) {
		super(errorCode, message);
	}

}
