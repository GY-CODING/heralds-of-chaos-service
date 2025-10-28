package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import lombok.AllArgsConstructor;
import org.gycoding.heraldsofchaos.application.service.CharacterService;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CharacterControllerMapper;
import org.gycoding.quasar.exceptions.model.ServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
@AllArgsConstructor
public class CharacterDataController {
    private final CharacterService service;

    private final CharacterControllerMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacter(@PathVariable String id, @RequestParam String lang) throws ServiceException {
        return ResponseEntity.ok(mapper.toRSDTO(service.get(id, lang)));
    }

    @GetMapping("/list")
    public ResponseEntity<?> listCharacters(@RequestParam String lang) throws ServiceException {
        return ResponseEntity.ok(service.list(lang).stream().map(mapper::toRSDTO).toList());
    }

    @GetMapping("/page")
    public ResponseEntity<?> pageCharacters(Pageable pageable, @RequestParam String lang) throws ServiceException {
        return ResponseEntity.ok(service.page(pageable, lang).getContent());
    }
}
