package unioeste.smch.core.services;

import unioeste.smch.core.entities.User;
import unioeste.smch.core.repositories.UserRepository;
import unioeste.smch.errors.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        }
        catch (RuntimeException e) {
            throw new ServiceException("Não foi possível salvar o usuário");
        }
    }

    public void delete(Long id) {
        try {
            userRepository.delete(id);
        }
        catch (RuntimeException e) {
            throw new ServiceException("Não foi possível remover o usuário");
        }
    }

    public User find(Long id) {
        return userRepository.findOne(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllByResponsibleId(Long id) {
        User user = userRepository.findOne(id);
        List<User> patients = null;
        if (user != null) {
            patients = userRepository.findAllByResponsible(user);
        }
        return patients;
    }
  
}
