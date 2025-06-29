package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.logs_pedidosEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface logs_pedidosRepository extends MongoRepository<logs_pedidosEntity, String> {

}
