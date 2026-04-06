package kz.kop_flowers.service;

import kz.kop_flowers.model.dto.FlowerDto;

import java.util.List;

public interface FlowerService {

    List<FlowerDto> getAllFlowers();

    FlowerDto getFlowerById(Integer id);

    FlowerDto createFlower(FlowerDto flower);

    void deleteFlowerById(Integer id);

    List<FlowerDto> getFlowersByCategoryId(Integer categoryId);

    FlowerDto updateFlower(Integer id, FlowerDto flowerDto);
}
