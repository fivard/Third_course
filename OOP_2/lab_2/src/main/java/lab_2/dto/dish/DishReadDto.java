package lab_2.dto.dish;

import lombok.Value;

@Value
public class DishReadDto {
    Integer id;
    String name;
    Double price;
    String description;
    Integer ordered;
}
