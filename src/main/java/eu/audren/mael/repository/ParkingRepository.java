package eu.audren.mael.repository;

import eu.audren.mael.repository.domain.ParkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingEntity, Long> {
}
