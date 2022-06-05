package lab_2.service.converter;

import lab_2.dto.order.OrderDishDto;
import lab_2.dto.order.OrderReadDto;
import lab_2.dto.order.OrderWriteDto;
import lab_2.persistence.entity.Order;
import lab_2.persistence.entity.OrderDish;
import lab_2.persistence.entity.OrderDishPK;
import lab_2.persistence.repository.DishRepository;
import lab_2.service.UserService;
import lab_2.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderConverter implements Converter<OrderReadDto, OrderWriteDto, Order> {

    private DishRepository wareRepository;
    private UserService userService;
    private OrderMapper orderMapper;

    @Override
    public OrderReadDto entityToDto(Order order) {
        var orderDto = orderMapper.entityToDto(order);
        orderDto.setTotalSum(order.getDishes().stream()
                .map(orderDish -> orderDish.getQuantity() * orderDish.getDish().getPrice())
                .reduce(0.0, Double::sum));
        var orderDishes = order.getDishes().stream()
                .map(orderDish -> new OrderDishDto()
                        .setDishName(orderDish.getDish().getName())
                        .setQuantity(orderDish.getQuantity()))
                .collect(Collectors.toList());

        return orderDto.setDishes(orderDishes);
    }

    @Override
    public Order dtoToEntity(OrderWriteDto dto) {
        var order = orderMapper.dtoToEntity(dto);
        order.setUser(userService.findByUsername(dto.getUsername())
                .orElseThrow(IllegalArgumentException::new));
        var orderDishMap = dto.getDishes().stream()
                .collect(Collectors.toMap(OrderDishDto::getDishName, OrderDishDto::getQuantity));
        var dishes = wareRepository.findAllByNameIn(dto.getDishes()
                .stream()
                .map(OrderDishDto::getDishName)
                .collect(Collectors.toList()));
        dishes.forEach(dish -> dish.incrementOrdered(orderDishMap.get(dish.getName())));
        wareRepository.saveAll(dishes);
        var orderDishes = dishes.stream()
                .map(dish -> new OrderDish()
                        .setId(new OrderDishPK()
                                .setOrder(order)
                                .setDish(dish))
                        .setQuantity(orderDishMap.get(dish.getName())))
                .collect(Collectors.toList());
        return order.setDishes(orderDishes);
    }
}
