package com.tbd.DeliveryMedicamentos.controllers;

import com.tbd.DeliveryMedicamentos.DTO.PedidoConDetallesDTO;
import com.tbd.DeliveryMedicamentos.DTO.PedidosDTO;
import com.tbd.DeliveryMedicamentos.DTO.ResumenPedidoClienteDTO;
import com.tbd.DeliveryMedicamentos.DTO.RutasZonasCruzadasDTO;
import com.tbd.DeliveryMedicamentos.entities.PedidosEntity;
import com.tbd.DeliveryMedicamentos.services.PedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {
    private final PedidosService pedidoService;

    @Autowired
    public PedidosController(PedidosService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<PedidosEntity>> getAllPedidos() {
        List<PedidosEntity> pedidos = pedidoService.getAllPedidos();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/rutas-zonas-cruzadas")
    public ResponseEntity<List<RutasZonasCruzadasDTO>> getRutasZonasCruzadas() {
        List<RutasZonasCruzadasDTO> resultado = pedidoService.rutasZonasCruzadas();
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }


    @GetMapping("/{id}/ruta")
    public ResponseEntity<String> getRutaEstimada(@PathVariable int id) {
        String rutaGeoJson = pedidoService.mostrarRuta(id);
        return rutaGeoJson != null ?
                ResponseEntity.ok(rutaGeoJson) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruta no encontrada");
    }

    @GetMapping("/{id}/ruta/multilinestring")
    public ResponseEntity<String> getRutaEstimadaMultiLineString(@PathVariable int id) {
        String rutaGeoJson = pedidoService.mostrarRutaMultiLineString(id);
        return rutaGeoJson != null ?
                ResponseEntity.ok(rutaGeoJson) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruta no encontrada");
    }


    @GetMapping("/urgentes/medio-pago-frecuente")
    public ResponseEntity<?> obtenerMedioPagoMasUsadoEnUrgentes() {
        Map<String, Object> resultado = pedidoService.obtenerMedioPagoMasUsadoEnUrgentes();
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PedidosEntity> getPedidoById(@PathVariable int id) {
        PedidosEntity pedido = pedidoService.getPedidoById(id);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PedidosEntity> createPedido(@RequestBody PedidosDTO pedido) {
        PedidosEntity nuevoPedido = pedidoService.createPedido(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @PostMapping("/registrar")
    public void registrarPedido(@RequestBody PedidoConDetallesDTO pedidoDTO) {
        pedidoService.registrarPedido(pedidoDTO.getPedido(), pedidoDTO.getDetalles());
    }

    @GetMapping("/resumen")
    public List<ResumenPedidoClienteDTO> getResumen() {
        return pedidoService.obtenerResumen();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidosEntity> updatePedido(@PathVariable int id, @RequestBody PedidosEntity pedido) {
        pedido.setId(id);
        PedidosEntity pedidoActualizado = pedidoService.updatePedido(pedido);
        return new ResponseEntity<>(pedidoActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable int id) {
        pedidoService.deletePedido(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/tiempo-promedio-repartidor")
    public ResponseEntity<List<Map<String, Object>>> tiempoPromedioPorRepartidor() {
        return ResponseEntity.ok(pedidoService.tiempoPromedioPorRepartidor());
    }

    @GetMapping("/tiempo-promedio-repartidor/{id}")
    public ResponseEntity<List<Map<String, Object>>> tiempoPromedioPorRepartidorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.tiempoPromedioPorRepartidorId(id));
    }

    @PostMapping("/{id}/cambiar-estado")
    public void cambiarEstadoPedido(
            @PathVariable int id,
            @RequestParam String nuevoEstado) {

        pedidoService.cambiarEstadoPedido(id, nuevoEstado);
    }

    @GetMapping("/medio-pago-urgente")
    public ResponseEntity<Map<String, Object>> medioPagoMasUsadoEnUrgencias() {
        Map<String, Object> result = pedidoService.medioPagoMasUsadoEnUrgencias();
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/contar-mes")
    public ResponseEntity<Map<String, Long>> getPedidosMesActual() {
        long count = pedidoService.contarPedidosMesActual();
        return ResponseEntity.ok(Collections.singletonMap("count", count));
    }

    @GetMapping("/farmacias-mas-fallidas")
    public ResponseEntity<List<Map<String, Object>>> farmaciasConMasEntregasFallidas() {
        return ResponseEntity.ok(pedidoService.farmaciasConMasEntregasFallidas());
    }

    @GetMapping("/cliente-mas-gastador")
    public ResponseEntity<Map<String, Object>> getClienteConMasGasto() {
        Map<String, Object> result = pedidoService.clienteConMasGastoEnPedidosEntregados();
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @GetMapping("/pedidos-con-alerta")
    public ResponseEntity<List<Integer>> pedidosConAlertas() {
        return ResponseEntity.ok(pedidoService.obtenerPedidosConNotificaciones());
    }

    @PutMapping("/actualizarEntrega/{id}")
    public ResponseEntity<String> actualizarEstadoPedido(@PathVariable int id, @RequestBody Map<String, String> body) {
        String estadoEntrega = body.get("estado_entrega");
        if (estadoEntrega == null) {
            return new ResponseEntity<>("Debe proporcionar el estado de entrega.", HttpStatus.BAD_REQUEST);
        }
        if (pedidoService.actualizarEstadoEntregaPedido(id, estadoEntrega)) {
            return new ResponseEntity<>("Estado del pedido actualizado.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo actualizar el estado del pedido.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tardados")
    public List<Integer> getPedidosTardados() {
        return pedidoService.obtenerPedidosTardados();
    }

    @PostMapping("/confirmar/{id}")
    public void confirmarPedido(@PathVariable("id") int pedidoId) {
        pedidoService.descontarStock(pedidoId);
    }

}