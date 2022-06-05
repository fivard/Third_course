package lab_2.persistence.repository;

import lab_2.persistence.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findAllByNameIn(Collection<String> ids);
}
