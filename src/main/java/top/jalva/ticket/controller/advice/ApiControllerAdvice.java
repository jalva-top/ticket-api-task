package top.jalva.ticket.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import top.jalva.ticket.controller.dto.IdTicketResponse;
import top.jalva.ticket.exception.ApiException;

@ControllerAdvice
public class ApiControllerAdvice {
	
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<IdTicketResponse> handleException(ApiException e) {
        return new ResponseEntity<>(new IdTicketResponse(e), HttpStatus.OK);
    }

}