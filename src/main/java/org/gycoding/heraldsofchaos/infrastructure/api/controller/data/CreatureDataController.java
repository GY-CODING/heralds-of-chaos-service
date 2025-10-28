package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import lombok.AllArgsConstructor;
import org.gycoding.heraldsofchaos.application.service.CreatureService;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CreatureControllerMapper;
import org.gycoding.quasar.exceptions.model.ServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creatures")
@AllArgsConstructor
public class CreatureDataController {
    private final CreatureService service;

    private final CreatureControllerMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCreature(@PathVariable String id, @RequestParam String lang) throws ServiceException {
        return ResponseEntity.ok(mapper.toRSDTO(service.get(id, lang)));
    }

    @GetMapping("/list")
    public ResponseEntity<?> listCreatures(@RequestParam String lang) throws ServiceException {
        return ResponseEntity.ok(service.list(lang).stream().map(mapper::toRSDTO).toList());
    }

    @GetMapping("/page")
    public ResponseEntity<?> pageCreatures(Pageable pageable, @RequestParam String lang) throws ServiceException {
        return ResponseEntity.ok(service.page(pageable, lang).getContent());
    }
}
