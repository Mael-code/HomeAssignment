package eu.audren.mael.rest;

import eu.audren.mael.model.Bill;
import eu.audren.mael.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bill/")
public class BillRest {

    @Autowired
    private BillService billService;

    /**
     * Get request to gather all persisted bills
     *
     * @return the list of all bills
     */

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public List<Bill> getAllBills(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> numberOfBill) {
        return billService.retrieveBills(page, numberOfBill);
    }
}
