package eu.audren.mael.service;

import eu.audren.mael.exception.DuplicateCarException;
import eu.audren.mael.exception.ResourceNotFound;
import eu.audren.mael.model.Bill;
import eu.audren.mael.model.Car;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.model.pricing.PerHoursPolicy;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.repository.CarRepository;
import eu.audren.mael.repository.ParkingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParkingServiceTest {

    @InjectMocks
    private ParkingService parkingService;

    @Mock
    private ParkingRepository parkingRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private CarRepository carRepository;

    @Test
    public void parkCarTest(){
        long parkingId = 1L;
        Car carToPark = new Car("immatriculation",parkingId,SlotType.STANDARDS);
        Parking savedParking = new Parking(parkingId,1,1,1,new PerHoursPolicy(1));

        Car parkedCar = new Car("immatriculation",parkingId,SlotType.STANDARDS);
        parkedCar.setParkingUsed(savedParking);

        when(parkingRepository.existsById(parkingId)).thenReturn(true);
        when(carRepository.existsCarByImmatriculation(carToPark.getImmatriculation())).thenReturn(false);
        when(parkingRepository.getOne(parkingId)).thenReturn(savedParking);
        when(carRepository.save(parkedCar)).thenReturn(parkedCar);

        Car savedCar = parkingService.parkCar(carToPark);
        assertThat(savedCar.getArrivalTime()).isNotEqualTo(0L);
        assertThat(savedCar.getParkingId()).isEqualTo(parkingId);
        assertThat(savedCar.getImmatriculation()).isEqualTo(carToPark.getImmatriculation());
        assertThat(savedCar.getParkingUsed().getStandardsSlotsUsed()).hasSize(1);
        assertThat(savedCar.getParkingUsed().getElectricSlots20KwUsed()).hasSize(0);
        assertThat(savedCar.getParkingUsed().getElectricSlots50KwUsed()).hasSize(0);
        assertThat(savedCar.getParkingUsed().getStandardsSlotsUsed()).containsExactly(savedCar);
        assertThat(savedCar.getBill()).isWithin(0.001f).of(0f);
    }

    @Test
    public void leaveSlotTest(){
        String immatriculation = "immatriculation";
        long parkingId = 1L;
        long arrivalTime = 0L;
        Parking savedParking = new Parking(parkingId,1,1,1,new PerHoursPolicy(1));
        Car parkedCar = new Car(immatriculation ,SlotType.ELECTRIC_50KW, savedParking, arrivalTime,0L);

        when(carRepository.existsCarByImmatriculation(immatriculation)).thenReturn(true);
        when(carRepository.findOneByImmatriculation(immatriculation)).thenReturn(parkedCar);

        Car leavingCar = parkingService.leaveSlot(immatriculation);

        assertThat(leavingCar.getImmatriculation()).isEqualTo(immatriculation);
        assertThat(leavingCar.getSlotType()).isEquivalentAccordingToCompareTo(SlotType.ELECTRIC_50KW);
        assertThat(leavingCar.getParkingId()).isEqualTo(parkingId);
        assertThat(leavingCar.getArrivalTime()).isEqualTo(arrivalTime);
        assertThat(leavingCar.getDepartureTime()).isNotEqualTo(0L);
        assertThat(leavingCar.getBill()).isNotWithin(0.1f).of(0f);

        verify(billRepository,times(1)).save(new Bill(parkedCar));

    }

    @Test(expected = ResourceNotFound.class)
    public void deleteParkingWithBadId(){
        long badId = 1;
        when(parkingRepository.existsById(badId)).thenReturn(false);
        parkingService.deleteParking(badId);
    }

    @Test(expected = ResourceNotFound.class)
    public void parkCarTestWithBadParkingId(){
        long badId = 1;
        when(parkingRepository.existsById(badId)).thenReturn(false);
        parkingService.parkCar(new Car("immatriculation",badId, SlotType.ELECTRIC_20KW));
    }

    @Test(expected = DuplicateCarException.class)
    public void parkCarTestWithExistingCar(){
        String existingImmatriculation = "badImmatriculation";
        long existingParking = 1L;
        when(carRepository.existsCarByImmatriculation(existingImmatriculation)).thenReturn(true);
        when(parkingRepository.existsById(existingParking)).thenReturn(true);
        parkingService.parkCar(new Car(existingImmatriculation,existingParking, SlotType.ELECTRIC_20KW));
    }

    @Test(expected = DuplicateCarException.class)
    public void testThatCarDoNotParkWhenAllSlotsAreTaken(){
        long parkingId = 1L;
        Car carToPark = new Car("immatriculation",parkingId,SlotType.ELECTRIC_50KW);
        Parking savedParking = new Parking(parkingId,1,1,0,new PerHoursPolicy(1));

        Car parkedCar = new Car("immatriculation",parkingId,SlotType.STANDARDS);
        parkedCar.setParkingUsed(savedParking);

        when(parkingRepository.existsById(parkingId)).thenReturn(true);
        when(carRepository.existsCarByImmatriculation(carToPark.getImmatriculation())).thenReturn(false);
        when(parkingRepository.getOne(parkingId)).thenReturn(savedParking);

        parkingService.parkCar(carToPark);
    }

}
