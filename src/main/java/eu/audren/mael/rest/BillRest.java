package eu.audren.mael.rest;

import eu.audren.mael.model.Bill;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/bill/")
public class BillRest {

    @Autowired
    private BillRepository billRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Bill> getAllBills(){
        return billRepository.findAll();
    }
}
