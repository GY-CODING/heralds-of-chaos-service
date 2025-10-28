package org.gycoding.heraldsofchaos.infrastructure.api.controller.management;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.gycoding.heraldsofchaos.application.service.PlaceService;
import org.gycoding.heraldsofchaos.application.service.WorldService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.worlds.PlaceRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.worlds.WorldRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.PlaceControllerMapper;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.WorldControllerMapper;
import org.gycoding.quasar.exceptions.model.QuasarException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worlds")
@AllArgsConstructor
public class WorldManagementController {
    private final WorldService worldService;

    private final WorldControllerMapper worldMapper;

    private final PlaceService placeService;

    private final PlaceControllerMapper placeMapper;

    @PostMapping("")
    public ResponseEntity<?> save(@Valid @RequestBody WorldRQDTO world) throws QuasarException {
        return ResponseEntity.ok(worldMapper.toRSDTO(worldService.save(worldMapper.toIDTO(world))));
    }

    @PatchMapping("")
    public ResponseEntity<?> update(@Valid @RequestBody WorldRQDTO world, @RequestParam String id) throws QuasarException {
        return ResponseEntity.ok(worldMapper.toRSDTO(worldService.update(worldMapper.toIDTO(world, id))));
    }

    @DeleteMapping("")
    public ResponseEntity<?> removeWorld(@RequestParam String id) throws QuasarException {
        worldService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/places")
    public ResponseEntity<?> save(@Valid @RequestBody PlaceRQDTO place) throws QuasarException {
        return ResponseEntity.ok(placeMapper.toRSDTO(placeService.save(placeMapper.toIDTO(place))));
    }

    @PatchMapping("/places")
    public ResponseEntity<?> update(@Valid @RequestBody PlaceRQDTO place, @RequestParam String id) throws QuasarException {
        return ResponseEntity.ok(placeMapper.toRSDTO(placeService.update(placeMapper.toIDTO(place, id))));
    }

    @DeleteMapping("/places")
    public ResponseEntity<?> removePlace(@RequestParam String id) throws QuasarException {
        placeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
