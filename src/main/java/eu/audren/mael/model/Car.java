package eu.audren.mael.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.audren.mael.repository.domain.CarEntity;
import lombok.*;

import javax.persistence.*;

/**
 * The car representation
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "immatriculation")
public class Car {

    @JsonProperty
    private String immatriculation;

    @JsonProperty
    private SlotType slotType;

    @Setter
    @JsonIgnore
    private Parking parkingUsed;

    @JsonProperty
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

    public Car(String immatriculation){
        this.immatriculation = immatriculation;
    }

    public Car(CarEntity carEntity){
        this.immatriculation = carEntity.getImmatriculation();
        this.arrivalTime = carEntity.getArrivalTime();
        this.slotType = carEntity.getSlotType();
        this.parkingUsed = new Parking(carEntity.getParkingUsed());
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
