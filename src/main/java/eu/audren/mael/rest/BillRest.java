package eu.audren.mael.rest;

import eu.audren.mael.model.Bill;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bill/")
public class BillRest {

    @Autowired
    private BillRepository billRepository;

    /**
     * Get request to gather all persisted bills
     * @return the list of all bills
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Bill> getAllBills(){
        return billRepository.findAll().stream().map(Bill::new).collect(Collectors.toList());
    }
}
