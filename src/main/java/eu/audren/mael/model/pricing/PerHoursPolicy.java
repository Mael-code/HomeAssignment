package eu.audren.mael.model.pricing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("perHours")
public class PerHoursPolicy extends PricingPolicy {

    @JsonProperty
    private int hourPrice;

    @JsonCreator
    public PerHoursPolicy(@JsonProperty("hourPrice") int hourPrice){
        this.hourPrice = hourPrice;
    }

    @Override
    public float getPricing(float numberOfHoursSpent) {
        return numberOfHoursSpent*hourPrice;
    }
}
