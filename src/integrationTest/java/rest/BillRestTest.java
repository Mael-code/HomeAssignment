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
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void getAllBillsTest() {
        Car car = parkThenRemoveCar("immatriculation", SlotType.STANDARDS);
        List<Bill> bills = billRest.getAllBills(Optional.empty(), Optional.empty());
        assertThat(bills).hasSize(1);
        Bill carBill = bills.get(0);
        assertThat(carBill.getId()).isNotNull();
        assertThatBillHaveTheSameValueThanTheCar(carBill, car);
    }

    @Test
    public void getBillsWithPaginationTest() {
        Car car1 = parkThenRemoveCar("immatriculation1", SlotType.STANDARDS);
        Car car2 = parkThenRemoveCar("immatriculation2", SlotType.ELECTRIC_20KW);
        Car car3 = parkThenRemoveCar("immatriculation3", SlotType.ELECTRIC_50KW);

        List<Bill> bills = billRest.getAllBills(Optional.empty(), Optional.of(2));
        assertThat(bills).hasSize(2);
        assertThat(bills.stream().map(Bill::getCarImmatriculation).collect(Collectors.toList()))
                .containsExactly(car1.getImmatriculation(), car2.getImmatriculation());

        bills = billRest.getAllBills(Optional.of(1), Optional.of(2));
        assertThat(bills).hasSize(1);
        assertThat(bills.stream().map(Bill::getCarImmatriculation).collect(Collectors.toList()))
                .containsExactly(car3.getImmatriculation());

        assertThat(billRest.getAllBills(Optional.of(1), Optional.of(4))).isEmpty();

        bills = billRest.getAllBills(Optional.of(0), Optional.of(4));
        assertThat(bills).hasSize(3);
        assertThat(bills.stream().map(Bill::getCarImmatriculation).collect(Collectors.toList()))
                .containsExactly(car1.getImmatriculation(), car2.getImmatriculation(), car3.getImmatriculation());

        Bill firstCarBill = getFirstCarBillFromCarImmatriculation(bills, car1);
        Bill secondCarBill = getFirstCarBillFromCarImmatriculation(bills, car2);
        Bill thirdCarBill = getFirstCarBillFromCarImmatriculation(bills, car3);

        assertThatBillHaveTheSameValueThanTheCar(firstCarBill, car1);
        assertThatBillHaveTheSameValueThanTheCar(secondCarBill, car2);
        assertThatBillHaveTheSameValueThanTheCar(thirdCarBill, car3);
    }

    private Car parkThenRemoveCar(String immatriculation, SlotType slotType) {
        Car car = new Car(immatriculation, parkingEntity.getId(), slotType);
        carRest.parkCar(car);
        return carRest.removeCar(immatriculation);
    }

    private Bill getFirstCarBillFromCarImmatriculation(List<Bill> bills, Car car) {
        return bills.stream()
                .filter(bill -> bill.getCarImmatriculation().equals(car.getImmatriculation()))
                .findFirst().get();
    }

    private void assertThatBillHaveTheSameValueThanTheCar(Bill bill, Car car) {
        final float tolerance = 0.001f;
        assertThat(bill.getPrice()).isWithin(tolerance).of(car.getBill());
        assertThat(bill.getCarImmatriculation()).isEqualTo(car.getImmatriculation());
        assertThat(bill.getParkingId()).isEqualTo(car.getParkingId());
        assertThat(bill.getArrivalTime()).isEqualTo(car.getArrivalTime());
        assertThat(bill.getDepartureTime()).isEqualTo(car.getDepartureTime());
        assertThat(bill.getSlotType()).isEquivalentAccordingToCompareTo(car.getSlotType());
    }
}
