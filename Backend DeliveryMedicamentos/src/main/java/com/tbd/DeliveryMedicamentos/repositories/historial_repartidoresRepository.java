package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.historial_repartidoresEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface historial_repartidoresRepository extends MongoRepository<historial_repartidoresEntity, String> {
}
