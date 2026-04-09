package kz.kop_flowers.service;

import kz.kop_flowers.model.FlowerMapper;
import kz.kop_flowers.model.dto.CategoryDto;
import kz.kop_flowers.model.dto.FlowerDto;
import kz.kop_flowers.model.entity.Category;
import kz.kop_flowers.model.entity.Flower;
import kz.kop_flowers.model.exception.FlowerNotFoundException;
import kz.kop_flowers.repository.FlowerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlowerServiceImplTest {

    @Mock
    private FlowerRepository flowerRepository;

    @Mock
    private FlowerMapper mapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private FlowerServiceImpl flowerService;

    @Test
    void getAllFlowers_success_mapsEachEntity() {
        List<Flower> flowers = List.of(
                Flower.builder().id(1).name("Роза").build(),
                Flower.builder().id(2).name("Пионы").build()
        );

        List<FlowerDto> flowerDtoList = List.of(
                FlowerDto.builder().id(1).name("Роза").build(),
                FlowerDto.builder().id(2).name("Пионы").build()
        );

        Mockito.when(flowerRepository.findAll()).thenReturn(flowers);
        Mockito.when(mapper.fromEntityToDto(flowers.get(0))).thenReturn(flowerDtoList.get(0));
        Mockito.when(mapper.fromEntityToDto(flowers.get(1))).thenReturn(flowerDtoList.get(1));

        List<FlowerDto> result = flowerService.getAllFlowers();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Роза", result.get(0).getName());

        verify(flowerRepository).findAll();
        verify(mapper).fromEntityToDto(flowers.get(0));
        verify(mapper).fromEntityToDto(flowers.get(1));
        verifyNoMoreInteractions(flowerRepository, mapper);
        verifyNoInteractions(categoryService);
    }

    @Test
    void getFlowerById_whenNotExists_throws() {
        Mockito.when(flowerRepository.findById(123)).thenReturn(Optional.empty());

        Assertions.assertThrows(FlowerNotFoundException.class, () -> flowerService.getFlowerById(123));

        verify(flowerRepository).findById(123);
        verifyNoMoreInteractions(flowerRepository);
        verifyNoInteractions(mapper, categoryService);
    }

    @Test
    void createFlower_success_savesAndMaps() {
        Category category = Category.builder().id(1).name("8 марта").build();

        FlowerDto inputFlowerDto = FlowerDto.builder()
                .name("Роза")
                .price(BigDecimal.valueOf(100))
                .size("M")
                .category(CategoryDto.builder().id(1).build())
                .build();

        Flower savedFlower = Flower.builder()
                .id(1)
                .name("Роза")
                .price(BigDecimal.valueOf(100))
                .size("M")
                .category(category)
                .build();

        FlowerDto savedFlowerDto = FlowerDto.builder()
                .id(1)
                .name("Роза")
                .price(BigDecimal.valueOf(100))
                .size("M")
                .category(CategoryDto.builder().id(1).name("8 марта").build())
                .build();

        Mockito.when(categoryService.getCategoryById(1)).thenReturn(category);
        Mockito.when(flowerRepository.save(any(Flower.class))).thenReturn(savedFlower);
        Mockito.when(mapper.fromEntityToDto(savedFlower)).thenReturn(savedFlowerDto);

        FlowerDto result = flowerService.createFlower(inputFlowerDto);

        Assertions.assertEquals("Роза", result.getName());
        Assertions.assertEquals(1, result.getCategory().getId());

        ArgumentCaptor<Flower> captor = ArgumentCaptor.forClass(Flower.class);
        verify(categoryService).getCategoryById(1);
        verify(flowerRepository).save(captor.capture());
        Assertions.assertNull(captor.getValue().getId());
        Assertions.assertEquals("Роза", captor.getValue().getName());
        Assertions.assertEquals(category, captor.getValue().getCategory());
        verify(mapper).fromEntityToDto(savedFlower);
        verifyNoMoreInteractions(categoryService, flowerRepository, mapper);
    }
}

