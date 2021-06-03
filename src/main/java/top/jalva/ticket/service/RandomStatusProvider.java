package top.jalva.ticket.service;

import top.jalva.ticket.model.enums.PaymentStatus;

public interface RandomStatusProvider {

	PaymentStatus get();

	void setNextValue(PaymentStatus nextValue);

}
