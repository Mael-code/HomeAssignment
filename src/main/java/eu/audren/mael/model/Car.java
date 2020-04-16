package eu.audren.mael.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity(name = "CAR")
@Table(name = "CAR")
public class Car {

    @JsonProperty
    @Id
    @Column(name = "IMMATRICULATION")
    private final String immatriculation;

    @JsonProperty
    @Column(name = "ARRIVAL_TIME")
    private final long arrivalTime;

    @JsonProperty
    @Column(name = "SLOT_TYPE")
    @Enumerated(EnumType.STRING)
    private final SlotType slotType;

    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAR_IMMATRICULATION")
    private Parking parkingUsed;

    @Setter
    @JsonProperty
    private long departureTime;

    public Car(String immatriculation, long parkingId, SlotType slotType){
        this.immatriculation = immatriculation;
        this.parkingUsed = new Parking(parkingId);
        this.slotType = slotType;
        this.arrivalTime = System.currentTimeMillis();
    }

    @JsonProperty(value = "parkingId")
    public long getParkingId(){
        return parkingUsed.getId();
    }

    @JsonProperty(value = "bill")
    public float getBill(){
        if(departureTime!=0) {
            return parkingUsed.getPricingPolicy().getPricing(convertMilliSecondsToHour(arrivalTime - departureTime));
        }else {
            return 0;
        }
    }

    private float convertMilliSecondsToHour(long duration){
        return duration/1000*60*60;
    }

}
