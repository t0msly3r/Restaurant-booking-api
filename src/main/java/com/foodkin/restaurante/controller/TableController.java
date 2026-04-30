package com.foodkin.restaurante.controller;

import com.foodkin.restaurante.dto.TableDTO;
import com.foodkin.restaurante.service.TableService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TableController {
     private final TableService tableService;

     @SecurityRequirement(name = "bearerAuth")
     @GetMapping
     public ResponseEntity<List<TableDTO>> getAllTables() {
         return ResponseEntity.ok(tableService.getAllTables());
     }

     @SecurityRequirement(name = "bearerAuth")
     @GetMapping("/availables")
     public ResponseEntity<List<TableDTO>> getAvailable() {
         return ResponseEntity.ok(tableService.getAllTables());
     }

     @SecurityRequirement(name = "bearerAuth")
     @GetMapping("/{id}")
     public ResponseEntity<TableDTO> getById(@PathVariable String id) {
         return ResponseEntity.ok(tableService.getByID(id));
     }

     @SecurityRequirement(name = "bearerAuth")
     @PostMapping
     public ResponseEntity<TableDTO> create(@Valid @RequestBody TableDTO dto) {
         return ResponseEntity.status(HttpStatus.CREATED).body(tableService.create(dto));
     }

     @SecurityRequirement(name = "bearerAuth")
     @PutMapping("/{id}")
     public ResponseEntity<TableDTO> update(@PathVariable String id,
                                            @Valid @RequestBody TableDTO dto) {
         return ResponseEntity.ok(tableService.update(id, dto));
     }

     @SecurityRequirement(name = "bearerAuth")
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable String id) {
         tableService.delete(id);
         return ResponseEntity.noContent().build();
    }
}
