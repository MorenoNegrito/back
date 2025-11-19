package com.tienda.mascotas.api.Controller;
import com.tienda.mascotas.api.Model.Categoria;
import com.tienda.mascotas.api.Service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "Endpoints para gestionar categorías de productos")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(
            summary = "Obtener todas las categorías",
            description = "Retorna una lista con todas las categorías, activas e inactivas"
    )
    @ApiResponse(responseCode = "200", description = "Categorías obtenidas correctamente")
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @Operation(
            summary = "Obtener categoría por ID",
            description = "Retorna una categoría específica según su ID"
    )
    @ApiResponse(responseCode = "200", description = "Categoría encontrada")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(
            @Parameter(description = "ID de la categoría a buscar")
            @PathVariable Long id) {

        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Crear nueva categoría",
            description = "Crea una nueva categoría enviando los datos en el cuerpo de la petición"
    )
    @ApiResponse(responseCode = "201", description = "Categoría creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Categoria categoria) {
        try {
            Categoria nuevaCategoria = categoriaService.crear(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza una categoría existente según su ID"
    )
    @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Error al actualizar la categoría")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID de la categoría a actualizar")
            @PathVariable Long id,
            @RequestBody Categoria categoria) {

        try {
            Categoria categoriaActualizada = categoriaService.actualizar(id, categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina (o desactiva) una categoría según su ID"
    )
    @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente")
    @ApiResponse(responseCode = "400", description = "No se pudo eliminar la categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID de la categoría a eliminar")
            @PathVariable Long id) {

        try {
            categoriaService.eliminar(id);
            return ResponseEntity.ok().body("Categoría desactivada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Obtener categorías activas",
            description = "Retorna solo las categorías que están activas"
    )
    @ApiResponse(responseCode = "200", description = "Categorías activas obtenidas correctamente")
    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> obtenerActivas() {
        return ResponseEntity.ok(categoriaService.obtenerActivas());
    }
}
