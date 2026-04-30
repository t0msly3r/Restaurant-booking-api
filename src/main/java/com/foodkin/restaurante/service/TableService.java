package com.foodkin.restaurante.service;

import com.foodkin.restaurante.dto.TableDTO;
import com.foodkin.restaurante.exception.ResourceNotFound;
import com.foodkin.restaurante.exception.RestaurantException;
import com.foodkin.restaurante.model.Table;
import com.foodkin.restaurante.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;

    public List<TableDTO> getAllTables() {
        return tableRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TableDTO> getAvailable(String id) {
        return tableRepository.findById(id)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public TableDTO getByID(String id) {
        Table table = tableRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Table", id));
        return toDTO(table);
    }

    public TableDTO create(TableDTO dto) {
        if (tableRepository.existsByNumber(dto.getNumber())) {
            throw new RestaurantException(
                    "There is already a table with the number " + dto.getNumber(),
                    HttpStatus.CONFLICT
            );
        }
        Table table = new Table();
        table.setNumber(dto.getNumber());
        table.setCapacity(dto.getCapacity());
        table.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);
        return toDTO(tableRepository.save(table));
    }

    public TableDTO update(String id, TableDTO dto) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Table", id));
        tableRepository.findByNumber(dto.getNumber()).ifPresent(existingTable -> {
            if (!existingTable.getId().equals(id)) {
                throw new RestaurantException(
                        "There is already a table with the number " + dto.getNumber(),
                        HttpStatus.CONFLICT
                );
            }
        });
        table.setNumber(dto.getNumber());
        table.setCapacity(dto.getCapacity());
        table.setAvailable(dto.getAvailable());
        return toDTO(tableRepository.save(table));
    }

    public void delete(String id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFound("Table",id);
        }
        tableRepository.deleteById(id);
    }

    private TableDTO toDTO(Table table) {
        TableDTO dto = new TableDTO();
        dto.setId(table.getId());
        dto.setNumber(table.getNumber());
        dto.setCapacity(table.getCapacity());
        dto.setAvailable(table.getAvailable());
        return dto;
    }

}