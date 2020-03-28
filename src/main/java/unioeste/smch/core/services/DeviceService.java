package unioeste.smch.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unioeste.smch.core.entities.Device;
import unioeste.smch.core.repositories.DeviceRepository;
import unioeste.smch.errors.services.ServiceException;

import java.util.List;

@Service
public class DeviceService {
    

    @Autowired
    private DeviceRepository deviceRepository;

    // @Autowired
    // public DeviceService(DeviceRepository deviceRepository) {
    //     this.deviceRepository = deviceRepository;
    // }

    public Device save(Device device) {
        try {
            return deviceRepository.save(device);
        }
        catch (RuntimeException e) {
            throw new ServiceException("Não foi possível salvar o dispositivo");
        }
    }

    public void delete(Long id) {
        try {
            deviceRepository.delete(id);
        }
        catch (RuntimeException e) {
            throw new ServiceException("Não foi possível remover o dispositivo");
        }
    }

    public Device find(Long id) {
        return deviceRepository.findOne(id);
    }

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }
}
