package controller;

import lab_2.dto.dish.DishReadDto;
import lab_2.persistence.entity.Dish;
import lab_2.rest.controller.DishController;
import lab_2.service.DishService;
import lab_2.service.mapper.DishMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DishControllerTest {
    @InjectMocks
    private DishController controller;

    @Mock
    private DishService dishService;

    @Mock
    private DishMapper dishMapper;

    @Mock
    private List<DishReadDto> listDTO;

    @Mock
    private List<Dish> listE;

    @Test
    public void testGetAllUsers() {
        when(dishService.findAll()).thenReturn(listE);
        listDTO = listE.stream().
                map(dishMapper::entityToDto).
                collect(Collectors.toList());
        assertEquals(controller.findAll(), listDTO);
    }
}