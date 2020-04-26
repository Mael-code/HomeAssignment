package eu.audren.mael.service;

import eu.audren.mael.model.Bill;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.repository.domain.BillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public List<Bill> retrieveBills(Optional<Integer> page, Optional<Integer> numberOfBills) {
        List<BillEntity> billEntities;
        if (numberOfBills.isPresent()) {
            Pageable pageRequest = PageRequest.of(page.orElse(0), numberOfBills.get());
            billEntities = billRepository.findAll(pageRequest).get().collect(Collectors.toList());
        } else {
            billEntities = billRepository.findAll();
        }
        return billEntities.stream().map(Bill::new).collect(Collectors.toList());
    }
}
