package eu.audren.mael.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The bill representation contains all the payment information
 */
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Entity(name = "BILL")
@Table(name = "BILL")
public class Bill {

    @JsonProperty
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BILL_SEQUENCE")
    @SequenceGenerator(name = "BILL_SEQUENCE", sequenceName = "BILL_SEQUENCE")
    private long id;

    @JsonProperty
    @Column(name = "PRICE")
    private float price;

    @JsonProperty
    @Column(name = "CAR_IMMATRICULATION")
    private String carImmatriculation;

    @JsonProperty
    @Column(name = "PARKING_ID")
    private long parkingId;

    @JsonProperty
    @Column(name = "ARRIVAL_TIME")
    private long arrivalTime;

    @JsonProperty
    @Column(name = "DEPARTURE_TIME")
    private long departureTime;

    @JsonProperty
    @Column(name = "SLOT_TYPE")
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    public Bill(Car car){
        this.price = car.getBill();
        this.carImmatriculation = car.getImmatriculation();
        this.parkingId = car.getParkingId();
        this.arrivalTime = car.getArrivalTime();
        this.departureTime = car.getDepartureTime();
        this.slotType = car.getSlotType();
    }
}
