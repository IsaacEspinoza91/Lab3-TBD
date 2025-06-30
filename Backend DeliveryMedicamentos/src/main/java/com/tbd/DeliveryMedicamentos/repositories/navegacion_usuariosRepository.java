package com.tbd.DeliveryMedicamentos.repositories;

import com.tbd.DeliveryMedicamentos.entities.navegacion_usuariosEntity;
import com.tbd.DeliveryMedicamentos.DTO.ClienteSinCompraDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;

import java.util.List;

public interface navegacion_usuariosRepository extends MongoRepository<navegacion_usuariosEntity, String> {

    @Aggregation(pipeline = {
            //descomponer el array 'eventos' para procesar cada evento individualmente
            "{ '$unwind': '$eventos' }",
            //agrupar por cliente_id y recolectar los valores únicos de 'eventos.tipo'
            "{ '$group': { " +
                    "    '_id': '$cliente_id', " +
                    "    'acciones': { '$addToSet': '$eventos.tipo' } " +
                    "} }",
            //filtrar los clientes que tienen una acción de 'busqueda'
            //los clientes que no tienen 'busqueda' serán eliminados
            "{ '$match': { 'acciones': 'busqueda' } }",
            //de los clientes que pasaron el filtro anterior, eliminar los que si tienen una acción de 'compra'.
            //solo los que tienen 'busqueda' pero no 'compra' pasarán.
            "{ '$match': { 'acciones': { '$nin': ['compra'] } } }",
            //proyectar solo el ID del cliente para que coincida con el DTO.
            "{ '$project': { " +
                    "    '_id': 0, " +
                    "    'clienteId': '$_id' " +
                    "} }"
    })
    List<ClienteSinCompraDTO> encontrarClientesConBusquedaSinCompra();
}