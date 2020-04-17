package eu.audren.mael.service;

import eu.audren.mael.exception.DuplicateCarException;
import eu.audren.mael.exception.ResourceNotFound;
import eu.audren.mael.exception.ServerException;
import eu.audren.mael.model.Bill;
import eu.audren.mael.model.Car;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.repository.CarRepository;
import eu.audren.mael.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BillRepository billRepository;

    /**
     * Create a new Parking in the database
     * @param newParking the parking that will be saved
     * @return the created parking
     */
    public Parking createParking(Parking newParking){
        return parkingRepository.save(newParking);
    }

    /**
     * Gather all created parking
     * @return the list of created parking
     */
    public List<Parking> getAllParking(){
        return parkingRepository.findAll();
    }

    /**
     * Delete a persisted parking
     * @param id is the parking id that will be deleted
     * @return the deleted parking
     */
    @Transactional
    public Parking deleteParking(long id){
        checkThatParkingExists(id);
        return deleteParkingInRepository(id);
    }

    /**
     * Park the car into a parking if it meets the requirements
     * @param car is the car that will be parked
     * @return the persisted car
     */
    @Transactional
    public Car parkCar(Car car){
        checkThatParkingExists(car.getParkingId());
        checkThatCarIsNotParked(car.getImmatriculation());
        return parkCarInRepository(car);
    }

    /**
     * Remove the car from the parking slot it used
     * @param immatriculation the car immatriculation that will leave the parking
     * @return the leaving car
     */
    @Transactional
    public Car leaveSlot(String immatriculation){
        checkThatCarIsParked(immatriculation);
        Car car = carRepository.findOneByImmatriculation(immatriculation);
        carRepository.deleteByImmatriculation(immatriculation);
        car.setDepartureTime(System.currentTimeMillis());
        billRepository.save(new Bill(car));
        return car;
    }

    private Parking deleteParkingInRepository(long id){
        Parking parkingToDelete = parkingRepository.getOne(id);
        parkingRepository.delete(id);
        return parkingToDelete;
    }

    private Car parkCarInRepository(Car car){
        Parking parking = parkingRepository.getOne(car.getParkingId());
        checkRemainingSlots(parking,car);
        car.setParkingUsed(parking);
        car = carRepository.save(car);
        getParkingSlots(parking,car.getSlotType()).add(car);
        parkingRepository.save(parking);
        return car;
    }

    private List<Car> getParkingSlots(Parking parking, SlotType slotType){
        switch (slotType){
            case STANDARDS:
                return parking.getStandardsSlotsUsed();
            case ELECTRIC_20KW:
                return parking.getElectricSlots20KwUsed();
            case ELECTRIC_50KW:
                return parking.getElectricSlots50KwUsed();
            default:
                throw new ServerException(String.format("The slot type %s is unknown",slotType.name()));
        }
    }

    private void checkThatCarIsNotParked(String immatriculation){
        if (carRepository.existsCarByImmatriculation(immatriculation)){
            throw new DuplicateCarException(String.format("The car %s is already registered",immatriculation));
        }
    }

    private void checkThatCarIsParked(String immatriculation){
        if (! carRepository.existsCarByImmatriculation(immatriculation)){
            throw new ResourceNotFound(String.format("The car with the immatriculation %s is not parked",immatriculation));
        }
    }

    private void checkThatParkingExists(long parkingId){
        if (! parkingRepository.exists(parkingId)){
            throw new ResourceNotFound(String.format("Parking with the id %s was not found",parkingId));
        }
    }

    private void checkRemainingSlots(Parking parking, Car car){
        int slotsUsed;
        int slotNumber;
        switch (car.getSlotType()){
            case STANDARDS:
                slotsUsed = parking.getStandardsSlotsUsed().size();
                slotNumber = parking.getStandardSlots();
                break;
            case ELECTRIC_20KW:
                slotsUsed = parking.getElectricSlots20KwUsed().size();
                slotNumber = parking.getElectricSlots20Kw();
                break;
            case ELECTRIC_50KW:
                slotsUsed = parking.getElectricSlots50KwUsed().size();
                slotNumber = parking.getElectricSlots50Kw();
                break;
            default:
                throw new ServerException(String.format("The slot type %s is unknown",car.getSlotType().name()));
        }
        if (slotsUsed>=slotNumber){
            throw new DuplicateCarException(String.format("Slots %s are not available anymore for the parking %s",
                    car.getSlotType().name(),
                    parking.getId()));
        }

    }

}
