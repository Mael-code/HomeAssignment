package rest;

import eu.audren.mael.Application;
import eu.audren.mael.model.Bill;
import eu.audren.mael.model.Car;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.model.pricing.PerHoursPolicy;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.repository.CarRepository;
import eu.audren.mael.repository.ParkingRepository;
import eu.audren.mael.repository.domain.ParkingEntity;
import eu.audren.mael.rest.BillRest;
import eu.audren.mael.rest.CarRest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class BillRestTest {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private CarRest carRest;

    @Autowired
    private BillRest billRest;

    private ParkingEntity parkingEntity;

    @Before
    public void cleanDatabase() {
        parkingRepository.deleteAll();
        carRepository.deleteAll();
        billRepository.deleteAll();
        parkingEntity = new ParkingEntity(1L, 1, 1, 1, new PerHoursPolicy(60 * 60 * 100));
        parkingEntity = parkingRepository.save(parkingEntity);
    }


    @Test
    public void getBillTest() {
        String immatriculation = "immatriculation";
        Car car = new Car(immatriculation, parkingEntity.getId(), SlotType.STANDARDS);
        carRest.parkCar(car);
        car = carRest.removeCar(immatriculation);
        List<Bill> bills = billRest.getAllBills();
        assertThat(bills).hasSize(1);
        Bill carBill = bills.get(0);
        assertThat(carBill.getId()).isNotNull();
        assertThat(carBill.getCarImmatriculation()).isEqualTo(car.getImmatriculation());
        assertThat(carBill.getArrivalTime()).isEqualTo(car.getArrivalTime());
        assertThat(carBill.getDepartureTime()).isEqualTo(car.getDepartureTime());
        assertThat(carBill.getParkingId()).isEqualTo(car.getParkingId());
        assertThat(carBill.getSlotType()).isEquivalentAccordingToCompareTo(car.getSlotType());
        assertThat(carBill.getPrice()).isWithin(0.001f).of(car.getBill());
    }
}
