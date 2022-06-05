package lab_2.service;

import lab_2.persistence.entity.Dish;
import lab_2.persistence.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class DishService {

    private DishRepository dishRepository;

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }
}
