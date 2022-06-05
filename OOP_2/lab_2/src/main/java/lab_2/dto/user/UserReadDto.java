package lab_2.dto.user;

import lab_2.dto.order.OrderReadDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserReadDto {
    Integer id;
    String username;
    Integer balance;
    List<OrderReadDto> orders;
}
