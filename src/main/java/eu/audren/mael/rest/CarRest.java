package eu.audren.mael.rest;

import eu.audren.mael.model.Car;
import eu.audren.mael.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/car/")
public class CarRest {
    
    @Autowired
    private ParkingService parkingService;
    
    @RequestMapping(method = RequestMethod.POST)
    public Car parkCar(@RequestBody Car car){
        return parkingService.parkCar(car);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "{immatriculation}")
    public Car removeCar(@PathVariable String immatriculation){
        return parkingService.leaveSlot(immatriculation);
    }
}
