package com.tienda.mascotas.api.Controller;

import com.tienda.mascotas.api.Model.Usuario;
import com.tienda.mascotas.api.Service.UsuarioService;
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
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(
            @Parameter(description = "Email del usuario") @PathVariable String email) {
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.crear(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un usuario")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizar(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar (desactivar) un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.ok("Usuario desactivado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener usuarios activos")
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> obtenerActivos() {
        return ResponseEntity.ok(usuarioService.obtenerActivos());
    }

    @Operation(summary = "Buscar usuarios por nombre")
    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscar(
            @Parameter(description = "Nombre parcial del usuario") @RequestParam String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    @Operation(summary = "Cambiar role de un usuario")
    @PutMapping("/{id}/role")
    public ResponseEntity<?> cambiarRole(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @Parameter(description = "Nuevo role del usuario") @RequestParam Usuario.Role role) {
        try {
            Usuario usuario = usuarioService.cambiarRole(id, role);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Login de usuario")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean valido = usuarioService.validarCredenciales(request.getEmail(), request.getPassword());
        if (valido) {
            Usuario usuario = usuarioService.obtenerPorEmail(request.getEmail()).get();
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    static class LoginRequest {
        @Schema(description = "Email del usuario", example = "usuario@mail.com")
        private String email;
        @Schema(description = "Contraseña del usuario", example = "123456")
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}