package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.opiniones_clientesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface opiniones_clientesRepository extends MongoRepository<opiniones_clientesEntity, String>{

    List<opiniones_clientesEntity> findByClienteId(String clienteId);

    // Ejemplo: buscar por empresaId
    List<opiniones_clientesEntity> findByEmpresaId(String empresaId);

}

