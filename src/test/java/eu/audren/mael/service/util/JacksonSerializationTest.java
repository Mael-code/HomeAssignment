package eu.audren.mael.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.pricing.FixedAmountPolicy;
import eu.audren.mael.model.pricing.PerHoursPolicy;
import eu.audren.mael.model.pricing.PricingPolicy;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class JacksonSerializationTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void perHoursPolicySerializationTest() throws Exception {
        PricingPolicy perHoursPolicy = new PerHoursPolicy(2);
        String result = objectMapper.writeValueAsString(perHoursPolicy);
        PricingPolicy perHoursPolicy1 = objectMapper.readerFor(PricingPolicy.class).readValue(result);
        assertThat(perHoursPolicy1.getPricing(2)).isEqualTo(4);
    }

    @Test
    public void fixedAmountPolicySerializationTest() throws Exception {
        PricingPolicy fixedAmountPolicy= new FixedAmountPolicy(5,1);
        String result = objectMapper.writeValueAsString(fixedAmountPolicy);
        PricingPolicy perHoursPolicy1 = objectMapper.readerFor(PricingPolicy.class).readValue(result);
        assertThat(perHoursPolicy1.getPricing(2)).isEqualTo(7);
    }

    @Test
    public void parkingSerializationTest() throws Exception {
        PricingPolicy perHoursPolicy = new PerHoursPolicy(2);
        Parking parking = new Parking(1,2,3,perHoursPolicy);
        String result = objectMapper.writeValueAsString(parking);
        System.out.println(result);
        Parking deserializedParking = objectMapper.readerFor(Parking.class).readValue(result);
        assertThat(deserializedParking.getId()).isEqualTo(parking.getId());
        assertThat(deserializedParking.getPricingPolicy()).isEqualTo(parking.getPricingPolicy());
        assertThat(deserializedParking.getElectricSlots20Kw()).isEqualTo(parking.getElectricSlots20Kw());
        assertThat(deserializedParking.getElectricSlots50Kw()).isEqualTo(parking.getElectricSlots50Kw());
    }
}
