package eu.audren.mael.rest;

import eu.audren.mael.model.Parking;
import eu.audren.mael.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/parking/")
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
}
