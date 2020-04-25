package eu.audren.mael.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.audren.mael.model.pricing.PricingPolicy;
import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.type.SerializableToBlobType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The parking representation contains the parking properties and the car parked
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "PARKING")
@Table(name = "PARKING")
public class Parking {

    @JsonProperty
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARKING_SEQUENCE")
    @SequenceGenerator(name = "PARKING_SEQUENCE", sequenceName = "PARKING_SEQUENCE")
    private long id;

    @JsonProperty(required = true)
    @Column(name = "STANDARDS_SLOTS")
    private int standardSlots;

    @JsonProperty(required = true)
    @Column(name = "ELECTRIC_SLOTS_20KW")
    private int electricSlots20Kw;

    @JsonProperty(required = true)
    @Column(name = "ELECTRIC_SLOTS_50KW")
    private int electricSlots50Kw;

    @JsonProperty(required = true)
    @Column(name = "PRICING_POLICY")
    @Type(type = "org.hibernate.type.SerializableToBlobType", parameters = @Parameter(name = SerializableToBlobType.CLASS_NAME, value = "java.lang.Object"))
    private PricingPolicy pricingPolicy;

    @JsonIgnore
    @Setter
    @OneToMany(mappedBy = "immatriculation", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Car> standardsSlotsUsed;

    @JsonIgnore
    @Setter
    @OneToMany(mappedBy = "immatriculation", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Car> electricSlots20KwUsed;

    @JsonIgnore
    @Setter
    @OneToMany(mappedBy = "immatriculation", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Car> electricSlots50KwUsed;

    @JsonCreator
    public Parking(@JsonProperty("standardSlots") int standardSlots,
                   @JsonProperty("electricSlots20Kw") int electricSlots20Kw,
                   @JsonProperty("electricSlots50Kw") int electricSlots50Kw,
                   @JsonProperty("pricingPolicy") PricingPolicy pricingPolicy){
        this.standardSlots = standardSlots;
        this.electricSlots20Kw = electricSlots20Kw;
        this.electricSlots50Kw = electricSlots50Kw;
        this.pricingPolicy = pricingPolicy;
        this.standardsSlotsUsed = new ArrayList<>();
        this.electricSlots50KwUsed = new ArrayList<>();
        this.electricSlots20KwUsed = new ArrayList<>();
    }

    public Parking(long id, int standardSlots,
                   int electricSlots20Kw,
                   int electricSlots50Kw,
                   PricingPolicy pricingPolicy){
        this(standardSlots,electricSlots20Kw,electricSlots50Kw,pricingPolicy);
        this.id = id;
    }

    public Parking(long id){
        this.id = id;
    }
}
