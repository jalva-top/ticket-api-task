package top.jalva.ticket.exception;

public class InvalidRequestParameterException extends ApiException {

	private static final long serialVersionUID = -6019613841119455644L;

	public InvalidRequestParameterException(Integer errorCode, String message) {
		super(errorCode, message);
	}
}
