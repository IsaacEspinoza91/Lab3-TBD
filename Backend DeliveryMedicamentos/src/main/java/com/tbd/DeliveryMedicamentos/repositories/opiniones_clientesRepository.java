package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.DTO.OpinionesPorHoraDTO;
import com.tbd.DeliveryMedicamentos.entities.opiniones_clientesEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface opiniones_clientesRepository extends MongoRepository<opiniones_clientesEntity, String>{

    List<opiniones_clientesEntity> findByClienteId(String clienteId);

    // Ejemplo: buscar por empresaId
    List<opiniones_clientesEntity> findByEmpresaId(String empresaId);

    //Consulta 2 'busqueda en opiniones'
    @Query("{ 'comentario': { $regex: ?0, $options: 'i' } }")
    List<opiniones_clientesEntity> buscarPorComentarioInsensitive(String regex);

    // Consulta 6: Agrupar opiniones por hora segun puntacion
    @Aggregation(pipeline = {
            "{ '$group': { " +
                    "    '_id': { '$hour': '$fecha' }, " +
                    "    'promedioPuntuacion': { '$avg': '$puntuacion' }, " +
                    "    'totalOpiniones': { '$sum': 1 } " +
                    "} }",
            "{ '$project': { " +
                    "    'hora': '$_id', " +
                    "    'promedioPuntuacion': 1, " +
                    "    'totalOpiniones': 1, " +
                    "    '_id': 0 " +
                    "} }",
            "{ '$sort': { 'hora': 1 } }"
    })
    List<OpinionesPorHoraDTO> agruparOpinionesPorHora();
}

