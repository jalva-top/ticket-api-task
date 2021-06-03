package top.jalva.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import top.jalva.ticket.model.TicketOrder;
import top.jalva.ticket.model.enums.PaymentStatus;

public interface TicketOrderRepository extends JpaRepository<TicketOrder, Long>{

	Page<TicketOrder> findByPaymentStatus(PaymentStatus status, Pageable pageable);
}
