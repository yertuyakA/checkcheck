package kz.kop_flowers.repository;
import java.util.List;

import kz.kop_flowers.model.entity.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Integer> {
    List<Flower> findAllByCategoryId(Integer categoryId);

}

