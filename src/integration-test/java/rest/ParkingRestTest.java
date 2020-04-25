package rest;

import eu.audren.mael.Application;
import eu.audren.mael.exception.ResourceNotFound;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.pricing.PerHoursPolicy;
import eu.audren.mael.model.pricing.PricingPolicy;
import eu.audren.mael.repository.ParkingRepository;
import eu.audren.mael.rest.ParkingRest;
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
@ContextConfiguration(classes = { Application.class })
@WebAppConfiguration
public class ParkingRestTest {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ParkingRest parkingRest;

    @Before
    public void cleanDatabase(){
        parkingRepository.deleteAll();
    }

    @Test
    public void createParkingTest(){
        final int standardSlots = 2;
        final int electricSlots20Kw = 3;
        final int electricSlots50Kw = 4;
        final PricingPolicy pricingPolicy = new PerHoursPolicy(5);

        Parking newParking = new Parking(standardSlots,electricSlots20Kw,electricSlots50Kw,pricingPolicy);
        Parking createdParking = parkingRest.createParking(newParking);
        assertThat(createdParking).isNotNull();
        assertThat(createdParking.getId()).isNotNull();
        assertThat(createdParking.getStandardSlots()).isEqualTo(standardSlots);
        assertThat(createdParking.getElectricSlots20Kw()).isEqualTo(electricSlots20Kw);
        assertThat(createdParking.getElectricSlots50Kw()).isEqualTo(electricSlots50Kw);
        assertThat(createdParking.getPricingPolicy()).isEqualTo(pricingPolicy);
    }

    @Test
    public void deleteParkingTest(){
        final int standardSlots = 2;
        final int electricSlots20Kw = 3;
        final int electricSlots50Kw = 4;
        final PricingPolicy pricingPolicy = new PerHoursPolicy(5);

        Parking newParking = parkingRest.createParking(new Parking(standardSlots,electricSlots20Kw,electricSlots50Kw,pricingPolicy));

        Parking deletedParking = parkingRest.deleteParking(newParking.getId());
        assertThat(deletedParking.getId()).isEqualTo(newParking.getId());
        assertThat(deletedParking.getStandardSlots()).isEqualTo(standardSlots);
        assertThat(deletedParking.getElectricSlots20Kw()).isEqualTo(electricSlots20Kw);
        assertThat(deletedParking.getElectricSlots50Kw()).isEqualTo(electricSlots50Kw);
        assertThat(deletedParking.getPricingPolicy()).isEqualTo(pricingPolicy);
    }

    @Test(expected = ResourceNotFound.class)
    public void deleteParkingNotFound(){
        parkingRest.deleteParking(1);
    }



}
