package top.jalva.ticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import top.jalva.ticket.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
	Optional<Route> findByNumber(String number);
}
