package eu.audren.mael.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.audren.mael.model.pricing.PricingPolicy;
import eu.audren.mael.repository.domain.CarEntity;
import eu.audren.mael.repository.domain.ParkingEntity;
import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.type.SerializableToBlobType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The parking representation contains the parking properties and the car parked
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Parking {

    @JsonProperty
    private long id;

    @JsonProperty(required = true)
    private int standardSlots;

    @JsonProperty(required = true)
    private int electricSlots20Kw;

    @JsonProperty(required = true)
    private int electricSlots50Kw;

    @JsonProperty(required = true)
    private PricingPolicy pricingPolicy;

    @JsonIgnore
    @Setter
    private List<Car> standardsSlotsUsed;

    @JsonIgnore
    @Setter
    private List<Car> electricSlots20KwUsed;

    @JsonIgnore
    @Setter
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

    public Parking(ParkingEntity parkingEntity){
        this.id = parkingEntity.getId();
        this.standardSlots = parkingEntity.getStandardSlots();
        this.electricSlots20Kw = parkingEntity.getElectricSlots20Kw();
        this.electricSlots50Kw = parkingEntity.getElectricSlots50Kw();
        this.pricingPolicy = parkingEntity.getPricingPolicy();
        this.standardsSlotsUsed = convertCarEntity(parkingEntity.getStandardsSlotsUsed());
        this.electricSlots20KwUsed = convertCarEntity(parkingEntity.getElectricSlots20KwUsed());
        this.electricSlots50KwUsed = convertCarEntity(parkingEntity.getElectricSlots50KwUsed());
    }

    public Parking(long id){
        this.id = id;
    }

    private List<Car> convertCarEntity(List<CarEntity> carEntities){
        if (carEntities.isEmpty()){
            return new ArrayList<>(0);
        }else {
            return carEntities.stream().map(carEntity -> new Car(carEntity.getImmatriculation())).collect(Collectors.toList());
        }
    }
}
