package eu.audren.mael.service;

import eu.audren.mael.exception.ResourceNotFound;
import eu.audren.mael.model.Parking;
import eu.audren.mael.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    public Parking createParking(Parking newParking){
        return parkingRepository.save(newParking);
    }

    public List<Parking> getAllParking(){
        return parkingRepository.findAll();
    }

    @Transactional
    public Parking deleteParking(long id){
        if (parkingRepository.exists(id)) {
            Parking parkingToDelete = parkingRepository.getOne(id);
            parkingRepository.delete(id);
            return parkingToDelete;
        }else {
            throw new ResourceNotFound(String.format("Parking with the id %s was not found",id));
        }
    }

}
