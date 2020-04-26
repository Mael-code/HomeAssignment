package eu.audren.mael.rest;

import eu.audren.mael.model.Parking;
import eu.audren.mael.model.pricing.PricingPolicy;
import eu.audren.mael.service.ParkingService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/parking/")
public class ParkingRest {

    @Autowired
    private ParkingService parkingService;

    /**
     * Get request to retrieve all persisted parking
     *
     * @return the list of parking persisted
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Parking> getAllParking() {
        return parkingService.getAllParking();
    }

    /**
     * Post request to create a new parking
     *
     * @param standardSlots     number of parking standard slots
     * @param electricSlots20Kw number of parking 20 KW electrics slots
     * @param electricSlots50Kw number of parking 50 KW electrics slots
     * @param pricingPolicy     the policy used for this parking to compute the price when the car leave
     * @return the persisted parking
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Parking createParking(@RequestParam(value = "Number of parking standard slots") int standardSlots,
                                 @RequestParam(value = "Number of parking 20 KW electrics slots") int electricSlots20Kw,
                                 @RequestParam(value = "Number of parking 50 KW electrics slots") int electricSlots50Kw,
                                 @ApiParam(value = "Pricing policy used for the parking", required = true) @RequestBody PricingPolicy pricingPolicy) {
        return parkingService.createParking(standardSlots, electricSlots20Kw, electricSlots50Kw, pricingPolicy);
    }


    /**
     * Delete request to remove a persisted parking
     *
     * @param id the id of the persisted parking
     * @return the deleted parking
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public Parking deleteParking(@PathVariable long id) {
        return parkingService.deleteParking(id);
    }
}
