package eu.audren.mael.model;

import eu.audren.mael.model.pricing.PerHoursPolicy;
import eu.audren.mael.model.pricing.PricingPolicy;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class CarTest {

    private Car car;

    @Before
    public void setUp() {
        PricingPolicy pricingPolicy = new PerHoursPolicy(1);
        Parking parking = new Parking(1, 1, 1, pricingPolicy);
        car = new Car("", 1, null);
        car.setParkingUsed(parking);
    }

    @Test
    public void getBillWithoutDepartureTest() {
        assertThat(car.getBill()).isWithin(new Float(0.001)).of(0);
    }

    @Test
    public void getBillWithDepartureTest() {
        final long twoHourAndHalf = 1000 * 60 * 60 * 2 + 30 * 60 * 1000;
        car.setDepartureTime(car.getArrivalTime() + twoHourAndHalf);
        assertThat(car.getBill()).isWithin(new Float(0.001)).of(new Float(2.5));
    }
}
