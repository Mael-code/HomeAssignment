package eu.audren.mael.rest;

import eu.audren.mael.model.Parking;
import eu.audren.mael.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/parking/")
public class ParkingRest {

    @Autowired
    private ParkingService parkingService;

    /**
     * Get request to retrieve all persisted parking
     * @return the list of parking persisted
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Parking> getAllParking(){
        return parkingService.getAllParking();
    }

    /**
     * Post request to create a new parking
     * @param newParking the parking that will be created
     * @return the persisted parking
     */
    @RequestMapping(method = RequestMethod.POST)
    public Parking createParking(@RequestBody Parking newParking){
        return parkingService.createParking(newParking);
    }

    /**
     * Delete request to remove a persisted parking
     * @param id the id of the persisted parking
     * @return the deleted parking
     */
    @RequestMapping(method = RequestMethod.DELETE, path ="{id}")
    public Parking deleteParking(@PathVariable long id){
        return parkingService.deleteParking(id);
    }
}
