package lab_2.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderDishDto {
    String dishName;
    Integer quantity;
}
