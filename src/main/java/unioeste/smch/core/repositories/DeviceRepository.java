package unioeste.smch.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unioeste.smch.core.entities.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device findBySerialNumber(String serialNumber);
}
