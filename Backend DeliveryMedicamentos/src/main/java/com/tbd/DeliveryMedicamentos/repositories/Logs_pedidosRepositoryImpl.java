package com.tbd.DeliveryMedicamentos.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class Logs_pedidosRepositoryImpl implements LogsPedidosRepositoryCustom{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public Logs_pedidosRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    //consulta 3: Contar cuántos pedidos tienen más de 3 cambios de estado en menos de 10 minutos.
    @Override
    public List<String> findPedidosConMasDe3CambiosEn10Min() {

        String mapFunction = "function() {" +
                " var timestamps = this.eventos.map(function(e) { return new Date(e.timestamp).getTime(); });" +
                " emit(this.pedido_id, timestamps);" +
                "}";

        String reduceFunction = "function(key, values) {" +
                " var allTimestamps = [].concat.apply([], values);" +
                " allTimestamps.sort();" +
                " for (var i = 0; i < allTimestamps.length - 3; i++) {" +
                "   var start = allTimestamps[i];" +
                "   var end = allTimestamps[i + 3];" +
                "   if ((end - start) <= 10 * 60 * 1000) {" +
                "     return true;" +
                "   }" +
                " }" +
                " return false;" +
                "}";

        MapReduceResults<MapReduceResult> results = mongoTemplate.mapReduce(
                "logs_pedidos",
                mapFunction,
                reduceFunction,
                MapReduceResult.class
        );

        return StreamSupport.stream(results.spliterator(), false)
                .filter(MapReduceResult::getValue)
                .map(MapReduceResult::getId)
                .collect(Collectors.toList());
    }

    public static class MapReduceResult {
        private String id;     // pedido_id
        private Boolean value; // true o false

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public Boolean getValue() { return value; }
        public void setValue(Boolean value) { this.value = value; }
    }
}
