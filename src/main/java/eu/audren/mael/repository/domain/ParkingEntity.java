package eu.audren.mael.repository.domain;

import eu.audren.mael.model.Parking;
import eu.audren.mael.model.pricing.PricingPolicy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.type.SerializableToBlobType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "PARKING")
@Table(name = "PARKING")
public class ParkingEntity implements Serializable  {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARKING_SEQUENCE")
    @SequenceGenerator(name = "PARKING_SEQUENCE", sequenceName = "PARKING_SEQUENCE")
    private long id;


    @Column(name = "STANDARDS_SLOTS")
    private int standardSlots;

    @Column(name = "ELECTRIC_SLOTS_20KW")
    private int electricSlots20Kw;

    @Column(name = "ELECTRIC_SLOTS_50KW")
    private int electricSlots50Kw;


    @Column(name = "PRICING_POLICY")
    @Type(type = "org.hibernate.type.SerializableToBlobType", parameters = @Parameter(name = SerializableToBlobType.CLASS_NAME, value = "java.lang.Object"))
    private PricingPolicy pricingPolicy;

    @Setter
    @OneToMany(mappedBy = "immatriculation", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<CarEntity> standardsSlotsUsed;

    @Setter
    @OneToMany(mappedBy = "immatriculation", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<CarEntity> electricSlots20KwUsed;

    @Setter
    @OneToMany(mappedBy = "immatriculation", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<CarEntity> electricSlots50KwUsed;

    public ParkingEntity(long id, int standardSlots, int electricSlots20Kw, int electricSlots50Kw, PricingPolicy pricingPolicy){
        this(standardSlots, electricSlots20Kw, electricSlots50Kw, pricingPolicy);
        this.id = id;
    }

    public ParkingEntity(Parking parking){
        this(parking.getStandardSlots(), parking.getElectricSlots20Kw(), parking.getElectricSlots50Kw(), parking.getPricingPolicy());
    }

    private ParkingEntity(int standardSlots, int electricSlots20Kw, int electricSlots50Kw, PricingPolicy pricingPolicy) {
        this.standardSlots = standardSlots;
        this.electricSlots20Kw = electricSlots20Kw;
        this.electricSlots50Kw = electricSlots50Kw;
        this.pricingPolicy = pricingPolicy;
        this.standardsSlotsUsed = new ArrayList<>();
        this.electricSlots20KwUsed = new ArrayList<>();
        this.electricSlots50KwUsed = new ArrayList<>();
    }
}
