package top.jalva.ticket.exception;


public class EmptyRequestParameterException extends InvalidRequestParameterException {

	private static final long serialVersionUID = 4298982348060728393L;

	public EmptyRequestParameterException(Integer errorCode, String message) {
		super(errorCode, message);
	}
}
