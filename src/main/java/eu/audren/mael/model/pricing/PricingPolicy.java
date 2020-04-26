package eu.audren.mael.model.pricing;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * An abstract visualization of the pricing policy enabling to easily create new pricing policies
 */
@ApiModel(subTypes = {FixedAmountPolicy.class, PerHoursPolicy.class}, discriminator = "policy")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "policy")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PerHoursPolicy.class, name = "perHours"),
        @JsonSubTypes.Type(value = FixedAmountPolicy.class, name = "fixedAmount")
})
@EqualsAndHashCode
public abstract class PricingPolicy implements Serializable {

    /**
     * Compute the parking price according to the time spent
     *
     * @param numberOfHoursSpent the time spent in the parking slot
     * @return the price for the time spent
     */
    public abstract float getPricing(float numberOfHoursSpent);
}
