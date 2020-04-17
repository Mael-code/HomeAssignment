package eu.audren.mael.model.pricing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The fixed amount price policy
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("fixedAmount")
public class FixedAmountPolicy extends PricingPolicy {

    @JsonProperty
    private int fixedAmount;

    @JsonProperty
    private int hourPrice;

    @JsonCreator
    public FixedAmountPolicy(@JsonProperty("fixedAmount") int fixedAmount,@JsonProperty("hourPrice") int hourPrice){
        this.fixedAmount = fixedAmount;
        this.hourPrice = hourPrice;
    }

    /**
     * Multiply the hours spent per the hour price and add a fixed amount to compute the price
     * @param numberOfHoursSpent the time spent in the parking slot
     * @return the price for the time spent
     */
    @Override
    public float getPricing(float numberOfHoursSpent) {
        return fixedAmount+hourPrice*numberOfHoursSpent;
    }
}
