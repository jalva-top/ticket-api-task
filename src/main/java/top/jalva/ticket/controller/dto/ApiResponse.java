package top.jalva.ticket.controller.dto;

import org.springframework.util.StringUtils;

public abstract class ApiResponse {
	
    protected String info = "";
    protected String error = "";
    protected Integer errorCode;

	public String getInfo() {
		return info;
	}

	public String getError() {
		return error;
	}

	public Integer getErrorCode() {
		return errorCode;
	}
	
	public boolean hasError() {
		return StringUtils.hasLength(error) || errorCode != null;
	}
}
