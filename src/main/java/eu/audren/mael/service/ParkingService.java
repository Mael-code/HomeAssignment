package eu.audren.mael.service;

import eu.audren.mael.model.Parking;
import eu.audren.mael.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
