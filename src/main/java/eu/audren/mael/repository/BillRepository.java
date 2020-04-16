package eu.audren.mael.repository;

import eu.audren.mael.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository  extends JpaRepository<Bill,Long> {
}
