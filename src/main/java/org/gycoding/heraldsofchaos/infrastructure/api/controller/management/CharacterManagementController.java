package org.gycoding.heraldsofchaos.infrastructure.api.controller.management;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.gycoding.heraldsofchaos.application.service.CharacterService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.characters.CharacterRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CharacterControllerMapper;
import org.gycoding.quasar.exceptions.model.QuasarException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
@AllArgsConstructor
public class CharacterManagementController {
    private final CharacterService service;

    private final CharacterControllerMapper mapper;

    @PostMapping("")
    public ResponseEntity<?> save(@Valid @RequestBody CharacterRQDTO character) throws QuasarException {
        return ResponseEntity.ok(mapper.toRSDTO(service.save(mapper.toIDTO(character))));
    }

    @PatchMapping("")
    public ResponseEntity<?> update(@Valid @RequestBody CharacterRQDTO character, @RequestParam String id) throws QuasarException {
        return ResponseEntity.ok(mapper.toRSDTO(service.update(mapper.toIDTO(character, id))));
    }

    @DeleteMapping("")
    public ResponseEntity<?> removeCharacter(@RequestParam String id) throws QuasarException {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
