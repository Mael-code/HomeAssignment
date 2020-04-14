package eu.audren.mael.model.pricing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @Override
    public int getPricing(int numberOfHoursSpent) {
        return fixedAmount+hourPrice*numberOfHoursSpent;
    }
}
