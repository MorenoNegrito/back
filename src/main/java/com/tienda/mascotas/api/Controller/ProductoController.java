package com.tienda.mascotas.api.Controller;

import com.tienda.mascotas.api.Model.Producto;
import com.tienda.mascotas.api.Service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener todos los productos")
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @Operation(summary = "Obtener un producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado",
                    content = @Content(schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "400", description = "Error en los datos del producto")
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crear(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un producto por ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizar(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar (desactivar) un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.ok("Producto desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener productos activos")
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> obtenerActivos() {
        return ResponseEntity.ok(productoService.obtenerActivos());
    }

    @Operation(summary = "Obtener productos destacados")
    @GetMapping("/destacados")
    public ResponseEntity<List<Producto>> obtenerDestacados() {
        return ResponseEntity.ok(productoService.obtenerDestacados());
    }

    @Operation(summary = "Obtener productos por categoría")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.obtenerPorCategoria(categoriaId));
    }

    @Operation(summary = "Buscar productos por nombre")
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscar(
            @Parameter(description = "Nombre parcial del producto") @RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @Operation(summary = "Obtener productos disponibles")
    @GetMapping("/disponibles")
    public ResponseEntity<List<Producto>> obtenerDisponibles() {
        return ResponseEntity.ok(productoService.obtenerDisponibles());
    }

    @Operation(summary = "Obtener productos recientes")
    @GetMapping("/recientes")
    public ResponseEntity<List<Producto>> obtenerRecientes() {
        return ResponseEntity.ok(productoService.obtenerRecientes());
    }

    @Operation(summary = "Marcar un producto como destacado")
    @PutMapping("/{id}/destacado")
    public ResponseEntity<?> marcarDestacado(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Parameter(description = "Indica si el producto será destacado") @RequestParam boolean destacado) {
        try {
            Producto producto = productoService.marcarComoDestacado(id, destacado);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar stock de un producto")
    @PutMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Parameter(description = "Cantidad de stock") @RequestParam Integer stock) {
        try {
            Producto producto = productoService.actualizarStock(id, stock);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}