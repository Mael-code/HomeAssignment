package eu.audren.mael.rest;

import eu.audren.mael.model.Car;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/parking/")
public class ParkingRest {

    @Autowired
    private ParkingService parkingService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Parking> getAllParking(){
        return parkingService.getAllParking();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Parking createParking(@RequestBody Parking newParking){
        return parkingService.createParking(newParking);
    }

    @RequestMapping(method = RequestMethod.DELETE, path ="{id}")
    public Parking deleteParking(@PathVariable long id){
        return parkingService.deleteParking(id);
    }
}
