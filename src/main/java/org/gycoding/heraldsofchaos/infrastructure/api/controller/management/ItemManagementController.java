package org.gycoding.heraldsofchaos.infrastructure.api.controller.management;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.gycoding.heraldsofchaos.application.service.ItemService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.items.ItemRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.ItemControllerMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemManagementController {
    private final ItemService service;

    private final ItemControllerMapper mapper;

    @PostMapping("")
    public ResponseEntity<?> save(@Valid @RequestBody ItemRQDTO item) throws Exception {
        return ResponseEntity.ok(mapper.toRSDTO(service.save(mapper.toIDTO(item))));
    }

    @PatchMapping("")
    public ResponseEntity<?> update(@Valid @RequestBody ItemRQDTO item, @RequestParam String id) throws Exception {
        return ResponseEntity.ok(mapper.toRSDTO(service.update(mapper.toIDTO(item, id))));
    }

    @DeleteMapping("")
    public ResponseEntity<?> removeItem(@RequestParam String id) throws Exception {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
