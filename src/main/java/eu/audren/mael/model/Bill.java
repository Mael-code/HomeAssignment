package eu.audren.mael.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.audren.mael.repository.domain.BillEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * The bill representation contains all the payment information
 */
@EqualsAndHashCode
@Getter
public class Bill {

    @JsonProperty
    private long id;

    @JsonProperty
    private float price;

    @JsonProperty
    private String carImmatriculation;

    @JsonProperty
    private long parkingId;

    @JsonProperty
    private long arrivalTime;

    @JsonProperty
    private long departureTime;

    @JsonProperty
    private SlotType slotType;

    public Bill(float price, String carImmatriculation, long parkingId, long arrivalTime, long departureTime, SlotType slotType) {
        this.price = price;
        this.carImmatriculation = carImmatriculation;
        this.parkingId = parkingId;
        this.departureTime = arrivalTime;
        this.arrivalTime = departureTime;
        this.slotType = slotType;
    }

    public Bill(BillEntity billEntity) {
        this(billEntity.getPrice(), billEntity.getCarImmatriculation(), billEntity.getParkingId(),
                billEntity.getDepartureTime(), billEntity.getArrivalTime(), billEntity.getSlotType());
        this.id = billEntity.getId();
    }

    public Bill(Car car) {
        this(car.getBill(), car.getImmatriculation(), car.getParkingId(),
                car.getDepartureTime(), car.getArrivalTime(), car.getSlotType());
    }
}
