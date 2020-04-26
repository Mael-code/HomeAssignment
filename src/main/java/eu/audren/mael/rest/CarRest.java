package eu.audren.mael.rest;

import eu.audren.mael.model.Car;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/car/")
public class CarRest {

    @Autowired
    private ParkingService parkingService;

    /**
     * Post request that persists a car in a parking slot
     *
     * @param immatriculation the car immatriculation
     * @param parkingUsed     the parking where the car intend to park
     * @param slotType        the kind of slot where the car will park
     * @return the persisted car
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Car parkCar(@RequestParam("immatriculation") String immatriculation,
                       @RequestParam("parkingUsed") long parkingUsed,
                       @RequestParam("slotType") SlotType slotType) {
        return parkingService.parkCar(immatriculation, parkingUsed, slotType);
    }

    /**
     * Delete request to remove a car from a parking slot
     *
     * @param immatriculation the car immatriculation that will leave the slot
     * @return the leaving car
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "{immatriculation}")
    @ResponseStatus(HttpStatus.OK)
    public Car removeCar(@PathVariable String immatriculation) {
        return parkingService.leaveSlot(immatriculation);
    }
}
