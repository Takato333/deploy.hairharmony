package com.example.demo.api;

import com.example.demo.entity.ServiceEntity;
import com.example.demo.model.ServiceRequest;
import com.example.demo.model.ServiceResponse;
import com.example.demo.service.ServiceEntityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ServiceAPI {
    @Autowired
    ServiceEntityService serviceEntityService;


    @PostMapping
    public ResponseEntity createService (@Valid @RequestBody ServiceRequest serviceRequest) {
        ServiceResponse newService = serviceEntityService.createService(serviceRequest);
        return ResponseEntity.ok(newService);
    }

    @GetMapping
    public ResponseEntity  getAllServices() {
        List<ServiceEntity> services = serviceEntityService.getAllServices();
        return ResponseEntity.ok(services);
    }

    //tìm service bằng id
    @GetMapping("{serviceId}")
    public ResponseEntity<ServiceEntity> getServiceById(@PathVariable UUID serviceId) {
        ServiceEntity service = serviceEntityService.getServiceById(serviceId);
        return ResponseEntity.ok(service);
    }


    //tìm service bằng name
    @GetMapping("/name:{serviceName}")
    public ResponseEntity getServiceByName(@PathVariable String serviceName){
        ServiceEntity service  = serviceEntityService.getServiceByName(serviceName);
        return ResponseEntity.ok(service);
    }

    //tìm các service có trong type
    @GetMapping("/type:{serviceType}")
    public ResponseEntity getServiceByType(@PathVariable String serviceType){
        List<ServiceEntity> services = serviceEntityService.getServiceByType(serviceType);
        return ResponseEntity.ok(services);
    }

    @PutMapping("{serviceId}")
    public ResponseEntity updateService(@PathVariable UUID serviceId, @Valid @RequestBody ServiceEntity service) {
        ServiceEntity updatedService = serviceEntityService.updateServiceById(serviceId, service);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("{serviceId}")
    public ResponseEntity deleteService(@PathVariable UUID serviceId) {
        ServiceEntity updatedService = serviceEntityService.deleteServiceById(serviceId);
        return ResponseEntity.ok(updatedService);
    }


}
