package top.jalva.ticket.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import top.jalva.ticket.controller.enums.ApiError;
import top.jalva.ticket.exception.ApiEntityNotFoundException;
import top.jalva.ticket.exception.IllegalOrderStateException;
import top.jalva.ticket.exception.PaymentErrorException;
import top.jalva.ticket.model.TicketOrder;
import top.jalva.ticket.model.enums.PaymentStatus;
import top.jalva.ticket.repository.TicketOrderRepository;
import top.jalva.ticket.service.RandomStatusProvider;
import top.jalva.ticket.service.TicketOrderPaymentService;

@Service
public class TicketOrderPaymentServiceImpl implements TicketOrderPaymentService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private TicketOrderRepository repo;
	private RandomStatusProvider randomStatusProvider;

	@Autowired
	public TicketOrderPaymentServiceImpl(TicketOrderRepository repo, RandomStatusProvider randomStatusProvider) {
		super();
		this.repo = repo;
		this.randomStatusProvider = randomStatusProvider;
	}

	@Override
	public Optional<TicketOrder> findOldestProcessingTicketOrder() {
		PageRequest pageable = PageRequest.of(0, 1, Sort.by("id"));
		List<TicketOrder> list = repo.findByPaymentStatus(PaymentStatus.PROCESSING, pageable).getContent();

		if(!list.isEmpty()) {
			TicketOrder ticket = list.get(0);
			if(log.isTraceEnabled()) {
				log.trace("Oldest ticket payment have to be posted has id={}", ticket.getId());
			}
			return Optional.of(ticket);
		}
		
		if(log.isTraceEnabled()) {
			log.trace("No orders to pay for tickets");
		}

		return Optional.empty();
	}

	@Override
	public TicketOrder pay(long id) {
		TicketOrder order = getTicketOrderById(id);
		throwIfIllegalStatus(order);

		PaymentStatus newPaymentStatus = randomStatusProvider.get();
		order.setPaymentStatus(newPaymentStatus);
		order = repo.save(order);
		
		if(newPaymentStatus == PaymentStatus.ERROR) {
			ApiError paymentError = ApiError.PAYMENT_ERROR;
			throw new PaymentErrorException(paymentError.getCode(), paymentError.getMessage("id=" + id));
		}
		
		return repo.save(order);
	}

	private void throwIfIllegalStatus(TicketOrder order) {
		if(!order.getPaymentStatus().isEditable()) {
			ApiError illegalOrderStatus = ApiError.ILLEGAL_ORDER_STATUS;
			throw new IllegalOrderStateException(illegalOrderStatus.getCode(), illegalOrderStatus.getMessage());
		}
	}

	private TicketOrder getTicketOrderById(long id) {
		ApiError orderDoesNotExist = ApiError.ORDER_DOES_NOT_EXIST;

		return repo.findById(id)//
				.orElseThrow(()-> new ApiEntityNotFoundException(orderDoesNotExist.getCode(), orderDoesNotExist.getMessage("id=" + id)));
	} 
}
