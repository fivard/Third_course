package lab_2.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrderWriteDto {
    String username;
    String status;
    List<OrderDishDto> dishes;
}
