package com.example.demo.repository;

import com.example.demo.entity.ServiceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceEntity, UUID> {
    ServiceEntity findServiceByIdAndIsDeletedFalse(UUID id);
    ServiceEntity findServiceByName(String name);
    List<ServiceEntity> findServiceByIsDeletedFalse();
    List<ServiceEntity> findServiceByType(String type);

    @Query("select p.name, COUNT(ad) as totalBooked" +
            " from AppointmentDetail ad JOIN ad.serviceEntity p" +
            " group by p.id" +
            " order by totalBooked desc ")
    List<Object[]> findTop3BestChoiceServices();
}
