package eu.audren.mael.repository;

import eu.audren.mael.repository.domain.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<BillEntity, Long> {
}
