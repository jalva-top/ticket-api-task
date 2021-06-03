package top.jalva.ticket.exception;

import java.util.Optional;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 4637554818196576644L;
	
	Optional<Integer> errorCode;
	
	public ApiException(String message) {
		super(message);
	}

	public ApiException(Integer errorCode, String message) {
		this(message);
		this.errorCode = Optional.ofNullable(errorCode);
	}

	public Optional<Integer> getErrorCode() {
		return errorCode;
	}
}
