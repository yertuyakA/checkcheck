package kz.kop_flowers.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FlowerDto {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String size;
    private CategoryDto category;
}
