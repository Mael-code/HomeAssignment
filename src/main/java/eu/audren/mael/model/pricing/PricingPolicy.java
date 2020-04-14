package eu.audren.mael.model.pricing;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "policy")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PerHoursPolicy.class, name="perHours"),
        @JsonSubTypes.Type(value = FixedAmountPolicy.class, name = "fixedAmount")
})
@EqualsAndHashCode
public abstract class PricingPolicy implements Serializable {

    public abstract int getPricing(int numberOfHoursSpent);
}
