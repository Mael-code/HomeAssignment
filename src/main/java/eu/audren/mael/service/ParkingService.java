package eu.audren.mael.service;

import eu.audren.mael.exception.DuplicateCarException;
import eu.audren.mael.exception.ResourceNotFound;
import eu.audren.mael.exception.ServerException;
import eu.audren.mael.model.Car;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.repository.CarRepository;
import eu.audren.mael.repository.ParkingRepository;
import eu.audren.mael.repository.domain.BillEntity;
import eu.audren.mael.repository.domain.CarEntity;
import eu.audren.mael.repository.domain.ParkingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
        return new Parking(parkingRepository.save(new ParkingEntity(newParking)));
    }

    /**
     * Gather all created parking
     * @return the list of created parking
     */
    public List<Parking> getAllParking(){
        return parkingRepository.findAll().stream().map(Parking::new).collect(Collectors.toList());
    }

    /**
     * Delete a persisted parking
     * @param id is the parking id that will be deleted
     * @return the deleted parking
     */
    @Transactional
    public Parking deleteParking(long id){
        Parking parkingToDelete = getParkingIfExist(id);
        parkingRepository.deleteById(id);
        return parkingToDelete;
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
        CarEntity carEntity = carRepository.findOneByImmatriculation(immatriculation);
        Car car = new Car(carEntity);

        carRepository.deleteByImmatriculation(immatriculation);
        car.setDepartureTime(System.currentTimeMillis());
        billRepository.save(new BillEntity(car));
        return car;
    }

    private Car parkCarInRepository(Car car){
        long parkingId = car.getParkingId();

        ParkingEntity parkingEntity =  parkingRepository.findById(car.getParkingId())
                .orElseThrow(() ->new ResourceNotFound(String.format("Parking with the id %d is not found",parkingId)));
        checkRemainingSlots(parkingEntity,car);

        CarEntity carEntity = carRepository.save(new CarEntity(car, parkingEntity));
        updateParkingSlots(parkingEntity,carEntity);
        carEntity.setParkingUsed(parkingEntity);
        parkingRepository.save(parkingEntity);
        return new Car(carEntity);
    }

    private Parking getParkingIfExist(long id){
        return parkingRepository.findById(id).map(Parking::new)
                .orElseThrow(() ->new ResourceNotFound(String.format("Parking with the id %d is not found",id)));
    }

    private void updateParkingSlots(ParkingEntity parking, CarEntity carEntity){
        switch (carEntity.getSlotType()){
            case STANDARDS:
                parking.getStandardsSlotsUsed().add(carEntity);
                break;
            case ELECTRIC_20KW:
                parking.getElectricSlots20KwUsed().add(carEntity);
                break;
            case ELECTRIC_50KW:
                parking.getElectricSlots50KwUsed().add(carEntity);
                break;
            default:
                throw new ServerException(String.format("The slot type %s is unknown",carEntity.getSlotType().name()));
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
        if (! parkingRepository.existsById(parkingId)){
            throw new ResourceNotFound(String.format("Parking with the id %s was not found",parkingId));
        }
    }

    private void checkRemainingSlots(ParkingEntity parking, Car car){
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
