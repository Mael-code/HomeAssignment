package eu.audren.mael.service;

import eu.audren.mael.exception.ResourceNotFound;
import eu.audren.mael.model.Car;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.repository.CarRepository;
import eu.audren.mael.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
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
        isParkingExisting(id);
        return deleteParkingInRepository(id);
    }

    public Car parkCar(Car car){
        isParkingExisting(car.getParkingId());
        isCarExisting(car.getImmatriculation());
        return parkCarInRepository(car);
    }

    public Car leaveSlot(String immatriculation){
        isCarExisting(immatriculation);
        Car car = carRepository.findOneByImmatriculation(immatriculation);
        carRepository.deleteByImmatriculation(immatriculation);
        return car;
    }

    private Parking deleteParkingInRepository(long id){
        Parking parkingToDelete = parkingRepository.getOne(id);
        parkingRepository.delete(id);
        return parkingToDelete;
    }

    @Transactional
    private Car parkCarInRepository(Car car){
        Parking parking = parkingRepository.getOne(car.getParkingId());
        car.setParkingUsed(parking);
        car = carRepository.save(car);
        switch (car.getSlotType()){
            case STANDARDS:
                parking.getStandardsSlotsUsed().add(car);
                break;
            case ELECTRIC_20KW:
                parking.getStandardsSlotsUsed().add(car);
                break;
            case ELECTRIC_50KW:
                parking.getStandardsSlotsUsed().add(car);
                break;
        }
        parkingRepository.save(parking);
        return car;
    }

    private void isCarExisting(String immatriculation){
        if (! carRepository.existsCarByImmatriculation(immatriculation)){
            throw new ResourceNotFound(String.format("Parking with the id %s was not found",immatriculation));
        }
    }

    private void isParkingExisting(long parkingId){
        if (! parkingRepository.exists(parkingId)){
            throw new ResourceNotFound(String.format("Parking with the id %s was not found",parkingId));
        }
    }
}
