package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.navegacion_usuariosEntity;
import com.tbd.DeliveryMedicamentos.DTO.ClienteSinCompraDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;

import java.util.List;

public interface navegacion_usuariosRepository extends MongoRepository<navegacion_usuariosEntity, String> {

    @Aggregation(pipeline = {
            //Descomponer el array 'eventos' para procesar cada evento individualmente.
            "{ '$unwind': '$eventos' }",
            //Agrupar por cliente_id y recolectar los valores únicos de 'eventos.tipo'.
            "{ '$group': { " +
                    "    '_id': '$cliente_id', " +
                    "    'acciones': { '$addToSet': '$eventos.tipo' } " +
                    "} }",
            //Filtrar los clientes que tienen una acción de 'busqueda'.
            //Los clientes que no tienen 'busqueda' serán eliminados.
            "{ '$match': { 'acciones': 'busqueda' } }", // <-- PRIMER FILTRO: Debe tener 'busqueda'
            //De los clientes que pasaron el filtro anterior, eliminar los que si tienen una acción de 'compra'.
            //Solo los que tienen 'busqueda' PERO NO 'compra' pasarán.
            "{ '$match': { 'acciones': { '$nin': ['compra'] } } }",
            //Proyectar solo el ID del cliente para que coincida con el DTO.
            "{ '$project': { " +
                    "    '_id': 0, " +
                    "    'clienteId': '$_id' " +
                    "} }"
    })
    List<ClienteSinCompraDTO> findClientesWithSearchAndNoPurchase();
}