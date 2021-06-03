package top.jalva.ticket.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;
import top.jalva.ticket.model.constant.ColumnNames;
import top.jalva.ticket.model.constant.ValidationMessages;

@Entity
@Data
@Table(name = "route")
public class Route {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ColumnNames.ID)
	Long id;
	
	@Size(min = 1, max = 32, message = ValidationMessages.ROUTE_NUMBER_SIZE)
	@Column(name = "number")
	String number;
}
