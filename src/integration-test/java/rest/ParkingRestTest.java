package rest;

import eu.audren.mael.Application;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.pricing.PerHoursPolicy;
import eu.audren.mael.model.pricing.PricingPolicy;
import eu.audren.mael.repository.ParkingRepository;
import eu.audren.mael.rest.ParkingRest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@WebIntegrationTest(randomPort = true)
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
}
