package eu.audren.mael.service;

import eu.audren.mael.exception.DuplicateCarException;
import eu.audren.mael.exception.ResourceNotFound;
import eu.audren.mael.model.Car;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.model.pricing.PerHoursPolicy;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.repository.CarRepository;
import eu.audren.mael.repository.ParkingRepository;
import eu.audren.mael.repository.domain.BillEntity;
import eu.audren.mael.repository.domain.CarEntity;
import eu.audren.mael.repository.domain.ParkingEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

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
    public void parkCarTest() {
        long parkingId = 1L;
        String immatriculation = "immatriculation";
        SlotType slotType = SlotType.STANDARDS;

        Car carToPark = new Car(immatriculation, parkingId, slotType);
        ParkingEntity emptyParking = new ParkingEntity(parkingId, 1, 1, 1, new PerHoursPolicy(1));

        Car parkedCar = new Car(immatriculation, parkingId, slotType);
        parkedCar.setParkingUsed(new Parking(emptyParking));

        CarEntity carEntity = new CarEntity(parkedCar, emptyParking);

        ParkingEntity usedParking = new ParkingEntity(parkingId, 1, 1, 1, new PerHoursPolicy(1));
        usedParking.getElectricSlots50KwUsed().add(carEntity);

        CarEntity parkedCarEntity = new CarEntity(parkedCar, usedParking);

        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(emptyParking));
        when(parkingRepository.existsById(parkingId)).thenReturn(true);
        when(parkingRepository.save(usedParking)).thenReturn(usedParking);

        when(carRepository.existsCarByImmatriculation(carToPark.getImmatriculation())).thenReturn(false);
        when(carRepository.save(parkedCarEntity)).thenReturn(parkedCarEntity);

        Car savedCar = parkingService.parkCar(immatriculation, parkingId, slotType);

        assertThat(savedCar.getArrivalTime()).isNotEqualTo(0L);
        assertThat(savedCar.getParkingId()).isEqualTo(parkingId);
        assertThat(savedCar.getImmatriculation()).isEqualTo(carToPark.getImmatriculation());
        assertThat(savedCar.getParkingUsed().getStandardSlotsUsed()).hasSize(1);
        assertThat(savedCar.getParkingUsed().getElectricSlots20KwUsed()).hasSize(0);
        assertThat(savedCar.getParkingUsed().getElectricSlots50KwUsed()).hasSize(0);
        assertThat(savedCar.getParkingUsed().getStandardSlotsUsed()).containsExactly(savedCar.getImmatriculation());
        assertThat(savedCar.getBill()).isWithin(0.001f).of(0f);
    }

    @Test
    public void leaveSlotTest() {
        String immatriculation = "immatriculation";
        long parkingId = 1L;
        long arrivalTime = 0L;
        ParkingEntity savedParking = new ParkingEntity(parkingId, 1, 1, 1, new PerHoursPolicy(1));
        CarEntity parkedCar = new CarEntity(immatriculation, SlotType.ELECTRIC_50KW, arrivalTime, savedParking);

        when(carRepository.existsCarByImmatriculation(immatriculation)).thenReturn(true);
        when(carRepository.findOneByImmatriculation(immatriculation)).thenReturn(parkedCar);

        Car leavingCar = parkingService.leaveSlot(immatriculation);

        assertThat(leavingCar.getImmatriculation()).isEqualTo(immatriculation);
        assertThat(leavingCar.getSlotType()).isEquivalentAccordingToCompareTo(SlotType.ELECTRIC_50KW);
        assertThat(leavingCar.getParkingId()).isEqualTo(parkingId);
        assertThat(leavingCar.getArrivalTime()).isEqualTo(arrivalTime);
        assertThat(leavingCar.getDepartureTime()).isNotEqualTo(0L);
        assertThat(leavingCar.getBill()).isNotWithin(0.1f).of(0f);

        verify(billRepository, times(1)).save(new BillEntity(new Car(parkedCar)));

    }

    @Test
    public void deleteParkingTest() {
        long parkingId = 1L;
        ParkingEntity parkingEntity = new ParkingEntity(parkingId, 1, 2, 3, new PerHoursPolicy(2));
        CarEntity carEntity = new CarEntity("immatriculation", SlotType.STANDARDS, 10L, parkingEntity);
        parkingEntity.setStandardsSlotsUsed(Collections.singletonList(carEntity));

        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(parkingEntity));

        Parking removedParking = parkingService.deleteParking(parkingId);

        assertThat(removedParking.getId()).isEqualTo(1L);
        assertThat(removedParking.getStandardSlots()).isEqualTo(1);
        assertThat(removedParking.getElectricSlots20Kw()).isEqualTo(2);
        assertThat(removedParking.getElectricSlots50Kw()).isEqualTo(3);
        assertThat(removedParking.getStandardSlotsUsed()).hasSize(1);
        assertThat(removedParking.getStandardSlotsUsed().get(0)).isEqualTo(carEntity.getImmatriculation());
        assertThat(removedParking.getElectricSlots20KwUsed()).hasSize(0);
        assertThat(removedParking.getElectricSlots50KwUsed()).hasSize(0);

        verify(parkingRepository, times(1)).deleteById(parkingId);
    }

    @Test(expected = ResourceNotFound.class)
    public void deleteParkingWithBadId() {
        long badId = 1;
        when(parkingRepository.findById(badId)).thenReturn(Optional.empty());
        parkingService.deleteParking(badId);
    }

    @Test(expected = ResourceNotFound.class)
    public void parkCarTestWithBadParkingId() {
        long badId = 1;
        when(parkingRepository.existsById(badId)).thenReturn(false);
        parkingService.parkCar("immatriculation", badId, SlotType.ELECTRIC_20KW);
    }

    @Test(expected = DuplicateCarException.class)
    public void parkCarTestWithExistingCar() {
        String existingImmatriculation = "badImmatriculation";
        long existingParking = 1L;
        when(carRepository.existsCarByImmatriculation(existingImmatriculation)).thenReturn(true);
        when(parkingRepository.existsById(existingParking)).thenReturn(true);
        parkingService.parkCar(existingImmatriculation, existingParking, SlotType.ELECTRIC_20KW);
    }

    @Test(expected = DuplicateCarException.class)
    public void testThatCarDoNotParkWhenAllSlotsAreTaken() {
        String immatriculation = "immatriculation";
        long parkingId = 1L;
        SlotType slotType = SlotType.ELECTRIC_50KW;
        Car carToPark = new Car(immatriculation, parkingId, SlotType.ELECTRIC_50KW);
        ParkingEntity savedParking = new ParkingEntity(parkingId, 1, 1, 0, new PerHoursPolicy(1));

        Car parkedCar = new Car(immatriculation, parkingId, SlotType.STANDARDS);
        parkedCar.setParkingUsed(new Parking(savedParking));

        when(parkingRepository.existsById(parkingId)).thenReturn(true);
        when(carRepository.existsCarByImmatriculation(carToPark.getImmatriculation())).thenReturn(false);
        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(savedParking));

        parkingService.parkCar(immatriculation, parkingId, slotType);
    }

}
