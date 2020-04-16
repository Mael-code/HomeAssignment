package eu.audren.mael.repository;

import eu.audren.mael.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    Car findOneByImmatriculation(String immatriculation);

    @Query("select case when count(c)> 0 then true else false end from CAR c where c.immatriculation = :immatriculation")
    boolean existsCarByImmatriculation(@Param("immatriculation") String immatriculation);

    long deleteByImmatriculation(String immatriculation);
}
