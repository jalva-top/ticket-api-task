package top.jalva.ticket.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.jalva.ticket.model.constant.ColumnNames;
import top.jalva.ticket.model.enums.PaymentStatus;
import top.jalva.ticket.model.listener.TicketOrderListener;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "ticket_order")
@EntityListeners(value = { TicketOrderListener.class })
public class TicketOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ColumnNames.ID)
	Long id;

	@NotNull
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	@Column(name = "departure")
	LocalDateTime departure;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id")
	Route route;

	@Setter
	@NotNull
	@Enumerated
	@Column(name = "status")
	PaymentStatus paymentStatus;
	
	@Version
	@Column(name = "version")
	long version;

	public TicketOrder(@NotNull LocalDateTime departure, @NotNull Route route) {
		super();
		this.departure = departure;
		this.route = route;
	}
}