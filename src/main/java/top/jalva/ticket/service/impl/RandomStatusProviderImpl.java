package top.jalva.ticket.service.impl;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import top.jalva.ticket.model.enums.PaymentStatus;
import top.jalva.ticket.service.RandomStatusProvider;

@Service
public class RandomStatusProviderImpl implements RandomStatusProvider {
	
	private static final int ERROR_MAX_RANDOM_NUMBER = 10;	
	private static final double ERROR_PROBABILITY = 0.2;
	private static final int RANDOM_MAX_RANGE_NUMBER = (int)(ERROR_MAX_RANDOM_NUMBER / ERROR_PROBABILITY);	
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private PaymentStatus nextValue;
	
	@Override
	public PaymentStatus get() {
		if(nextValue != null) {	
			PaymentStatus result = nextValue;
			nextValue = null;
			
			return result;
		}
		
		return generateRandom();
	}
	
	@Override
	public void setNextValue(PaymentStatus nextValue) {
		this.nextValue = nextValue;
	}

	private PaymentStatus generateRandom() {
		PaymentStatus result;
		int randomNumber = new Random().nextInt(RANDOM_MAX_RANGE_NUMBER);

		if(randomNumber < ERROR_MAX_RANDOM_NUMBER) {
			result = PaymentStatus.ERROR;
		} else {
			result = PaymentStatus.PAID;
		}	
		logRandomStatus(result);

		return result;
	}
	
	private void logRandomStatus(PaymentStatus status) {
		if(log.isTraceEnabled()) {
			log.trace("Random status: {}", status);
		}
	}
}
