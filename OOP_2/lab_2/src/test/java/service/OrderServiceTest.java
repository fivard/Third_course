package service;

import lab_2.persistence.entity.Order;
import lab_2.persistence.repository.OrderRepository;
import lab_2.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Order mockOrder;

    @Test
    public void testFindByOrderId() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(mockOrder));
        assertEquals(service.getByOrderId(1), Optional.of(mockOrder));
    }
}
