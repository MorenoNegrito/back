package com.tienda.mascotas.api.Controller;

import com.tienda.mascotas.api.Model.Pedido;
import com.tienda.mascotas.api.Service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con los pedidos de clientes")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(
            summary = "Obtener todos los pedidos",
            description = "Retorna una lista con todos los pedidos registrados"
    )
    @ApiResponse(responseCode = "200", description = "Pedidos obtenidos correctamente")
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    @Operation(
            summary = "Obtener pedido por ID",
            description = "Busca y retorna un pedido según su ID"
    )
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(
            @Parameter(description = "ID del pedido a buscar")
            @PathVariable Long id) {

        return pedidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Obtener pedidos por usuario",
            description = "Retorna los pedidos realizados por un usuario específico"
    )
    @ApiResponse(responseCode = "200", description = "Pedidos del usuario obtenidos correctamente")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerPorUsuario(
            @Parameter(description = "ID del usuario")
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(pedidoService.obtenerPorUsuario(usuarioId));
    }

    @Operation(
            summary = "Crear un nuevo pedido",
            description = "Permite crear un pedido enviando los datos en el cuerpo de la solicitud"
    )
    @ApiResponse(responseCode = "201", description = "Pedido creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pedido pedido) {
        try {
            Pedido nuevoPedido = pedidoService.crear(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Actualizar estado del pedido",
            description = "Actualiza el estado de un pedido (Ej: PENDIENTE, EN_PROCESO, ENTREGADO)"
    )
    @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Error al actualizar el estado")
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @Parameter(description = "ID del pedido a actualizar")
            @PathVariable Long id,

            @Parameter(description = "Nuevo estado del pedido")
            @RequestParam Pedido.EstadoPedido estado) {

        try {
            Pedido pedido = pedidoService.actualizarEstado(id, estado);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Obtener pedidos por estado",
            description = "Filtra los pedidos según su estado actual"
    )
    @ApiResponse(responseCode = "200", description = "Pedidos obtenidos correctamente")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> obtenerPorEstado(
            @Parameter(description = "Estado del pedido a filtrar")
            @PathVariable Pedido.EstadoPedido estado) {

        return ResponseEntity.ok(pedidoService.obtenerPorEstado(estado));
    }

    @Operation(
            summary = "Obtener pedidos dentro de un rango de fechas",
            description = "Devuelve los pedidos creados entre dos fechas determinadas"
    )
    @ApiResponse(responseCode = "200", description = "Pedidos filtrados correctamente")
    @GetMapping("/fechas")
    public ResponseEntity<List<Pedido>> obtenerPorFechas(
            @Parameter(description = "Fecha inicio (formato ISO: 2024-09-10T10:15:00)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,

            @Parameter(description = "Fecha fin (formato ISO: 2024-09-10T18:00:00)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(pedidoService.obtenerPorFechas(inicio, fin));
    }

    @Operation(
            summary = "Cancelar pedido",
            description = "Marca un pedido como cancelado"
    )
    @ApiResponse(responseCode = "200", description = "Pedido cancelado correctamente")
    @ApiResponse(responseCode = "400", description = "Error al cancelar pedido")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(
            @Parameter(description = "ID del pedido a cancelar")
            @PathVariable Long id) {

        try {
            pedidoService.cancelarPedido(id);
            return ResponseEntity.ok().body("Pedido cancelado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Contar pedidos por estado",
            description = "Retorna la cantidad de pedidos según su estado actual"
    )
    @ApiResponse(responseCode = "200", description = "Conteo obtenido correctamente")
    @GetMapping("/estadisticas/estado/{estado}")
    public ResponseEntity<Long> contarPorEstado(
            @Parameter(description = "Estado a contar")
            @PathVariable Pedido.EstadoPedido estado) {

        return ResponseEntity.ok(pedidoService.contarPorEstado(estado));
    }
}
