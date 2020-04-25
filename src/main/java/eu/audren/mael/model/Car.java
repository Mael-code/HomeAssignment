package eu.audren.mael.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

/**
 * The car representation
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "immatriculation")
@Entity(name = "CAR")
@Table(name = "CAR")
public class Car {

    @JsonProperty
    @Id
    @Column(name = "IMMATRICULATION")
    private String immatriculation;

    @JsonProperty
    @Column(name = "SLOT_TYPE")
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAR_IMMATRICULATION")
    private Parking parkingUsed;

    @JsonProperty
    @Column(name = "ARRIVAL_TIME")
    private long arrivalTime;

    @Setter
    @JsonProperty
    private long departureTime;

    @JsonCreator
    public Car(@JsonProperty("immatriculation") String immatriculation, @JsonProperty("parkingId") long parkingId,@JsonProperty("slotType") SlotType slotType){
        this.immatriculation = immatriculation;
        this.parkingUsed = new Parking(parkingId);
        this.slotType = slotType;
        this.arrivalTime = System.currentTimeMillis();
    }

    public Car(String immatriculation, SlotType slotType, Parking parking, long arrivalTime, long departureTime) {
        this.immatriculation = immatriculation;
        this.slotType = slotType;
        this.parkingUsed = parking;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    @JsonProperty(value = "parkingId")
    public long getParkingId(){
        return parkingUsed.getId();
    }

    /**
     * Compute the bill when the departure time is set according to the parking price policy
     * @return the computed the bill if the car left the slot otherwise return 0
     */
    @JsonProperty(value = "bill")
    public float getBill(){
        if(departureTime!=0) {
            return parkingUsed.getPricingPolicy().getPricing(convertMilliSecondsToHour(departureTime - arrivalTime));
        }else {
            return 0;
        }
    }

    private float convertMilliSecondsToHour(long duration){
        final long oneHour = 1000*60*60;
        return (float) duration/ (float) oneHour;
    }

}
