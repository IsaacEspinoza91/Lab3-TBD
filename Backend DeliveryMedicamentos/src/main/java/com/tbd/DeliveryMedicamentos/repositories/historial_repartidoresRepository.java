package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.RutaFrecuenteDTO;
import com.tbd.DeliveryMedicamentos.entities.historial_repartidoresEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface historial_repartidoresRepository extends MongoRepository<historial_repartidoresEntity, String> {
    @Aggregation(pipeline = {
            "{ '$unwind': '$rutas' }",
            "{ '$set': { 'timestamp_date': { '$toDate': '$rutas.timestamp' } } }",
            "{ '$match': { 'timestamp_date': { '$gte': ?0 } } }",
            "{ '$group': { '_id': { 'lat': '$rutas.lat', 'lng': '$rutas.lng' }, 'frecuencia': { '$sum': 1 } } }",
            "{ '$project': { '_id': 0, 'lat': '$_id.lat', 'lng': '$_id.lng', 'frecuencia': 1 } }",
            "{ '$sort': { 'frecuencia': -1 } }",
            "{ '$limit': 10 }"
    })
    List<RutaFrecuenteDTO> obtenerRutasFrecuentesDesde(Date fechaInicio);
}
