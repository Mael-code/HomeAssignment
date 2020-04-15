package eu.audren.mael.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.audren.mael.model.pricing.PricingPolicy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.type.SerializableToBlobType;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "PARKING")
@Table(name = "PARKING")
public class Parking {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARKING_SEQUENCE")
    @SequenceGenerator(name = "PARKING_SEQUENCE", sequenceName = "PARKING_SEQUENCE")
    @JsonProperty
    private long id;

    @Column(name = "STANDARDS_SLOTS")
    @JsonProperty
    private int standardSlots;

    @Column(name = "ELECTRIC_SLOTS_20KW")
    @JsonProperty
    private int electricSlots20Kw;

    @Column(name = "ELECTRIC_SLOTS_50KW")
    @JsonProperty
    private int electricSlots50Kw;

    @Column(name = "PRICING_POLICY")
    @JsonProperty
    @Type(type = "org.hibernate.type.SerializableToBlobType", parameters = @Parameter(name = SerializableToBlobType.CLASS_NAME, value = "java.lang.Object"))
    private PricingPolicy pricingPolicy;

    @JsonCreator
    public Parking(@JsonProperty("standardSlots") int standardSlots,
                   @JsonProperty("electricSlots20Kw") int electricSlots20Kw,
                   @JsonProperty("electricSlots50Kw") int electricSlots50Kw,
                   @JsonProperty("pricingPolicy") PricingPolicy pricingPolicy){
        this.standardSlots = standardSlots;
        this.electricSlots20Kw = electricSlots20Kw;
        this.electricSlots50Kw = electricSlots50Kw;
        this.pricingPolicy = pricingPolicy;
    }
}
