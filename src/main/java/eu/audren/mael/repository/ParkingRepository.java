package eu.audren.mael.repository;

import eu.audren.mael.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository  extends JpaRepository<Parking,Long> {
}
