package top.jalva.ticket.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.jalva.ticket.controller.enums.ApiError;
import top.jalva.ticket.exception.ApiEntityNotFoundException;
import top.jalva.ticket.exception.EmptyRequestParameterException;
import top.jalva.ticket.exception.InvalidRequestParameterException;
import top.jalva.ticket.model.Route;
import top.jalva.ticket.model.TicketOrder;
import top.jalva.ticket.repository.RouteRepository;
import top.jalva.ticket.repository.TicketOrderRepository;
import top.jalva.ticket.service.TicketOrderService;

@Service
public class TicketOrderServiceImpl implements TicketOrderService {
	
	private TicketOrderRepository repo;
	private RouteRepository routeRepo;

	@Autowired
	public TicketOrderServiceImpl(TicketOrderRepository repo, RouteRepository routeRepo) {
		super();
		this.repo = repo;
		this.routeRepo = routeRepo;
	};
	
	@Override
	public TicketOrder add(String routeNumber, LocalDateTime departure) {		
		checkDepartureThrowIfInvalid(departure);
		Route route = getRoute(routeNumber);

		return repo.save(new TicketOrder(departure, route));
	}

	private Route getRoute(String routeNumber) {
		if(routeNumber == null || routeNumber.isBlank()) {			
			ApiError emptyRequestParameter = ApiError.EMPTY_REQUEST_PARAMETER;
			throw new EmptyRequestParameterException(emptyRequestParameter.getCode(), emptyRequestParameter.getMessage("routeNumber"));
		}
		
		ApiError routeNotFound = ApiError.ROUTE_NOT_FOUND;
		return routeRepo.findByNumber(routeNumber)//
				.orElseThrow(()-> new ApiEntityNotFoundException(routeNotFound.getCode(), routeNotFound.getMessage()));
	}

	private void checkDepartureThrowIfInvalid(LocalDateTime departure) {
		if(departure == null) {			
			ApiError emptyRequestParameter = ApiError.EMPTY_REQUEST_PARAMETER;
			throw new EmptyRequestParameterException(emptyRequestParameter.getCode(), emptyRequestParameter.getMessage("departureDateTime"));
		}
		
		if(departure.isBefore(LocalDateTime.now())) {
			ApiError departureIsOutdated = ApiError.DEPARTURE_IS_OUTDATED;
			throw new InvalidRequestParameterException(departureIsOutdated.getCode(), departureIsOutdated.getMessage());	
		}
	}

	@Override
	public TicketOrder getById(long id) {
		ApiError ticketDoesNotExist = ApiError.ORDER_DOES_NOT_EXIST;
		return repo.findById(id)//
				.orElseThrow(()-> new ApiEntityNotFoundException(ticketDoesNotExist.getCode(), ticketDoesNotExist.getMessage()));
	}
}
