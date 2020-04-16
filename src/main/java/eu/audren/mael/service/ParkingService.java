package eu.audren.mael.service;

import eu.audren.mael.exception.DuplicateCarException;
import eu.audren.mael.exception.ResourceNotFound;
import eu.audren.mael.model.Car;
import eu.audren.mael.model.Parking;
import eu.audren.mael.repository.CarRepository;
import eu.audren.mael.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CarRepository carRepository;

    public Parking createParking(Parking newParking){
        return parkingRepository.save(newParking);
    }

    public List<Parking> getAllParking(){
        return parkingRepository.findAll();
    }

    @Transactional
    public Parking deleteParking(long id){
        checkThatParkingExists(id);
        return deleteParkingInRepository(id);
    }

    @Transactional
    public Car parkCar(Car car){
        checkThatParkingExists(car.getParkingId());
        checkThatCarIsNotParked(car.getImmatriculation());
        return parkCarInRepository(car);
    }

    @Transactional
    public Car leaveSlot(String immatriculation){
        checkThatCarIsParked(immatriculation);
        Car car = carRepository.findOneByImmatriculation(immatriculation);
        carRepository.deleteByImmatriculation(immatriculation);
        car.setDepartureTime(System.currentTimeMillis());
        return car;
    }

    private Parking deleteParkingInRepository(long id){
        Parking parkingToDelete = parkingRepository.getOne(id);
        parkingRepository.delete(id);
        return parkingToDelete;
    }

    private Car parkCarInRepository(Car car){
        Parking parking = parkingRepository.getOne(car.getParkingId());
        car.setParkingUsed(parking);
        car = carRepository.save(car);
        switch (car.getSlotType()){
            case STANDARDS:
                parking.getStandardsSlotsUsed().add(car);
                break;
            case ELECTRIC_20KW:
                parking.getElectricSlots20KwUsed().add(car);
                break;
            case ELECTRIC_50KW:
                parking.getElectricSlots50KwUsed().add(car);
                break;
        }
        parkingRepository.save(parking);
        return car;
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
}
