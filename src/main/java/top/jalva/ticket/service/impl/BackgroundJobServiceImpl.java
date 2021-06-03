package top.jalva.ticket.service.impl;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import top.jalva.ticket.controller.dto.IdTicketResponse;
import top.jalva.ticket.service.BackgroundJobService;

@Service
public class BackgroundJobServiceImpl implements BackgroundJobService {

	@Value("${payment-api.host}")
	private String apiHost;

	@Value("${payment-api.path}")
	private String apiPath;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private RestTemplate restTemplate;

	private final AtomicBoolean isActive = new AtomicBoolean(true);

	@Autowired
	public BackgroundJobServiceImpl(RestTemplate restTemplate) {
		super();
		this.restTemplate = restTemplate;
	}

	@Scheduled(fixedRate = 60_000)
	@Override
	public void handleTicketOrder() {
		if(isActive.get()) {
			findOrderAndPayIfExist();
		}
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive.getAndSet(isActive);
	}

	private void findOrderAndPayIfExist() {
		Optional<Long> id = getTicketOrderIdToPay();
		if(id.isPresent()) {
			logPaymentStart(id.get());
			payTicketOrder(id.get());
		} else {
			logNoTickets();
		}
	}

	private void logNoTickets() {
		if(log.isDebugEnabled()) {
			log.debug("No orders to pay for tickets");
		}
	}

	private void logPaymentStart(Long id) {
		if(log.isDebugEnabled()) {
			log.debug("Ticket order payment (id={}) start", id);
		}
	}

	private Optional<Long> getTicketOrderIdToPay() {
		IdTicketResponse apiResponse = restTemplate.getForObject(getUrl("/ticket-to-pay"), IdTicketResponse.class);

		if(apiResponse != null && apiResponse.hasTicket()) {
			return Optional.of(apiResponse.getTicket().getId());
		}

		return Optional.empty();
	}

	private void payTicketOrder(long ticketOrderId) {
		String url = getUrl("/tickets/" + ticketOrderId + "/payment");
		ResponseEntity<IdTicketResponse> paymentResponse = restTemplate.postForEntity(url, null, IdTicketResponse.class);
		if(paymentResponse.hasBody()) {
			log(ticketOrderId, paymentResponse.getBody());
		}
	}

	private void log(long id, IdTicketResponse apiResponse) {
		if(apiResponse.hasError()) {
			log.info("Ticket order payment (id={}) is failed with error {} ({})", id, apiResponse.getErrorCode(), apiResponse.getError());
		} else {
			log.info("Ticket order (id={}) is paid successfully", id);
		}
	} 

	private String getUrl(String requestPath) {
		return "http://" + apiHost + apiPath + requestPath;
	}
}
