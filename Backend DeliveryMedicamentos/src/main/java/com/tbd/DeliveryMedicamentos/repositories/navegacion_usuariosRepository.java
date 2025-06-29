package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.navegacion_usuariosEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface navegacion_usuariosRepository extends MongoRepository<navegacion_usuariosEntity, String> {
}

