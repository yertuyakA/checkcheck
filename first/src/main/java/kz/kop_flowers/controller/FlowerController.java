package kz.kop_flowers.controller;

import kz.kop_flowers.model.dto.FlowerDto;
import kz.kop_flowers.service.FlowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flowers")
@RequiredArgsConstructor
public class FlowerController {

    private final FlowerService flowerService;

    @GetMapping()
    public List<FlowerDto> getFlowers() {
        return flowerService.getAllFlowers();
    }

    @GetMapping("/{id}")
    public FlowerDto getFlowerById(
            @PathVariable Integer id
    ) {
        return flowerService.getFlowerById(id);
    }

    @PostMapping()
    public FlowerDto createFlower(
            @RequestBody FlowerDto flower
    ) {
        return flowerService.createFlower(flower);
    }

    @DeleteMapping("/{id}")
    public void deleteFlowerById(@PathVariable Integer id) {
        flowerService.deleteFlowerById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<FlowerDto> getFlowersByCategoryId(@PathVariable Integer categoryId) {
        return flowerService.getFlowersByCategoryId(categoryId);
    }

    @PutMapping("/{id}")
    public FlowerDto updateFlower(
            @PathVariable Integer id,
            @RequestBody FlowerDto flowerDto
    ) {
        return flowerService.updateFlower(id, flowerDto);
    }
}
