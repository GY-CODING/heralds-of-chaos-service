package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import lombok.AllArgsConstructor;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.service.PlaceService;
import org.gycoding.heraldsofchaos.application.service.WorldService;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.PlaceControllerMapper;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.WorldControllerMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worlds")
@AllArgsConstructor
public class WorldDataController {
    private final WorldService worldService;

    private final PlaceService placeService;

    private final WorldControllerMapper worldMapper;

    private final PlaceControllerMapper placeMapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorld(@PathVariable String id, @RequestParam String lang) throws APIException {
        return ResponseEntity.ok(worldMapper.toRSDTO(worldService.get(id, lang)));
    }

    @GetMapping("/list")
    public ResponseEntity<?> listWorlds(@RequestParam String lang) throws APIException {
        return ResponseEntity.ok(worldService.list(lang).stream().map(worldMapper::toRSDTO).toList());
    }

    @GetMapping("/page")
    public ResponseEntity<?> pageWorlds(Pageable pageable, @RequestParam String lang) throws APIException {
        return ResponseEntity.ok(worldService.page(pageable, lang).getContent());
    }

    @GetMapping("/places/{id}")
    public ResponseEntity<?> getPlace(@PathVariable String id, @RequestParam String lang) throws APIException {
        return ResponseEntity.ok(placeMapper.toRSDTO(placeService.get(id, lang)));
    }

    @GetMapping("/places/list")
    public ResponseEntity<?> listPlaces(@RequestParam String lang) throws APIException {
        return ResponseEntity.ok(placeService.list(lang).stream().map(placeMapper::toRSDTO).toList());
    }

    @GetMapping("/places/page")
    public ResponseEntity<?> pagePlaces(Pageable pageable, @RequestParam String lang) throws APIException {
        return ResponseEntity.ok(placeService.page(pageable, lang).getContent());
    }

    @GetMapping("/places/{id}/list")
    public ResponseEntity<?> listWorldPlaces(@PathVariable String id, @RequestParam String lang) throws APIException {
        return ResponseEntity.ok(worldService.listPlaces(id, lang).stream().map(placeMapper::toRSDTO).toList());
    }
}
