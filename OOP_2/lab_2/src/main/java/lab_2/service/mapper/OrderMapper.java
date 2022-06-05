package lab_2.service.mapper;

import lab_2.dto.order.OrderReadDto;
import lab_2.persistence.entity.Order;
import org.mapstruct.Mapper;
import lab_2.dto.order.OrderWriteDto;

@Mapper(componentModel = "spring", config = CommonMapper.class)
public interface OrderMapper extends CommonMapper<OrderReadDto, OrderWriteDto, Order> {
}
