package unioeste.smch.alerts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import unioeste.smch.alerts.entities.Alert;
import unioeste.smch.core.entities.User;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByVerified(Boolean verified);

    List<Alert> findAllByUserAndVerified(User user, boolean verified);

}
