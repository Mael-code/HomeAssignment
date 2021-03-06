package eu.audren.mael.model.pricing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The per hours pricing policy
 */
@ApiModel(parent = PricingPolicy.class)
@JsonTypeName("perHours")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PerHoursPolicy extends PricingPolicy {

    @JsonProperty
    private int hourPrice;

    @JsonCreator
    public PerHoursPolicy(@JsonProperty("hourPrice") int hourPrice) {
        this.hourPrice = hourPrice;
    }

    /**
     * Multiply the hour spent per hour price to get the price
     *
     * @param numberOfHoursSpent the time spent in the parking slot
     * @return the price for the time spent
     */
    @Override
    public float getPricing(float numberOfHoursSpent) {
        return numberOfHoursSpent * hourPrice;
    }
}
