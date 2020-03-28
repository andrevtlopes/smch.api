package unioeste.smch.alerts.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unioeste.smch.alerts.entities.Alert;
import unioeste.smch.alerts.repositories.AlertRepository;
import unioeste.smch.core.entities.User;
import unioeste.smch.core.repositories.UserRepository;
import unioeste.smch.errors.services.ServiceException;

import java.util.ArrayList;
import java.util.List;


@Service
public class AlertService {
    private final UserRepository userRepository;
    private AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository,
                        UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.userRepository = userRepository;
    }

    public Alert save(Alert alert) {
        try {
            return alertRepository.save(alert);
        }
        catch (RuntimeException e) {
            throw new ServiceException("Não foi possível salvar o alerta");
        }
    }

    public Alert setVerified(Long id) {
        try {
            Alert alert = alertRepository.findOne(id);
            alert.setVerified(true);
            return alertRepository.save(alert);
        }
        catch (RuntimeException e) {
            throw new ServiceException("Não foi possível salvar o alerta");
        }
    }

    public List<Alert> findAllByVerified(Boolean verified) {
        return alertRepository.findAllByVerified(verified);
    }

    public List<Alert> findAllByUsername(String username) {
        List<Alert> alerts = new ArrayList<>();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ServiceException("Usuário não encontrado");
        }

        alerts = alertRepository.findAllByUserAndVerified(user, false);

        List<User> patients = userRepository.findAllByResponsible(user);

        if (patients != null) {
            for (User patient: patients) {
                alerts.addAll(alertRepository.findAllByUserAndVerified(patient, false));
            }

        }

        return alerts;
    }
}
