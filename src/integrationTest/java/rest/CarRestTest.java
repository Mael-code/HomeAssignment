package rest;

import com.google.common.collect.Range;
import eu.audren.mael.Application;
import eu.audren.mael.model.Car;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.model.pricing.PerHoursPolicy;
import eu.audren.mael.repository.CarRepository;
import eu.audren.mael.repository.ParkingRepository;
import eu.audren.mael.repository.domain.ParkingEntity;
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

import static com.google.common.truth.Truth.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class CarRestTest {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarRest carRest;

    private ParkingEntity parkingEntity;

    @Before
    public void cleanDatabase() {
        parkingRepository.deleteAll();
        carRepository.deleteAll();
        parkingEntity = new ParkingEntity(1L, 1, 1, 1, new PerHoursPolicy(60 * 60 * 100));
        parkingEntity = parkingRepository.save(parkingEntity);
    }

    @Test
    public void parkCarTest() {
        String immatriculation = "immatriculation";
        long before = System.currentTimeMillis();
        Car car = carRest.parkCar(immatriculation, parkingEntity.getId(), SlotType.STANDARDS);
        long after = System.currentTimeMillis();
        assertThat(car.getSlotType()).isEquivalentAccordingToCompareTo(SlotType.STANDARDS);
        assertThat(car.getImmatriculation()).isEqualTo(immatriculation);
        assertThat(car.getArrivalTime()).isIn(Range.closed(before, after));
        assertThat(car.getDepartureTime()).isEqualTo(0L);
        assertThat(car.getBill()).isWithin(0.0001f).of(0);
        assertThat(car.getParkingId()).isEqualTo(parkingEntity.getId());
    }

    @Test
    public void leaveParkTest() throws Exception {
        String immatriculation = "immatriculation";
        long before = System.currentTimeMillis();
        Car car = carRest.parkCar(immatriculation, parkingEntity.getId(), SlotType.STANDARDS);
        Thread.sleep(1000);
        car = carRest.removeCar(immatriculation);
        long after = System.currentTimeMillis();
        assertThat(car.getParkingId()).isEqualTo(parkingEntity.getId());
        assertThat(car.getImmatriculation()).isEqualTo(immatriculation);
        assertThat(car.getArrivalTime()).isIn(Range.closed(before, after));
        assertThat(car.getDepartureTime()).isIn(Range.closed(before, after));
        assertThat(car.getBill()).isWithin(10f).of(100);
        System.out.println(car.getBill());
    }

}
