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

    /**
     * Post request that persists a car in a parking slot
     * @param car is the car that will be persisted
     * @return the persisted car
     */
    @RequestMapping(method = RequestMethod.POST)
    public Car parkCar(@RequestBody Car car){
        return parkingService.parkCar(car);
    }

    /**
     * Delete request to remove a car from a parking slot
     * @param immatriculation the car immatriculation that will leave the slot
     * @return the leaving car
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "{immatriculation}")
    public Car removeCar(@PathVariable String immatriculation){
        return parkingService.leaveSlot(immatriculation);
    }
}
