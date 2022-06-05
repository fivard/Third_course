package lab_2.service.mapper;

import lab_2.dto.dish.DishReadDto;
import lab_2.dto.dish.DishWriteDto;
import lab_2.persistence.entity.Dish;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapper.class)
public interface DishMapper extends CommonMapper<DishReadDto, DishWriteDto, Dish> {
}
