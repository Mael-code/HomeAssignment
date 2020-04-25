package eu.audren.mael.repository.domain;

import eu.audren.mael.model.Car;
import eu.audren.mael.model.SlotType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id")
@Entity(name = "BILL")
@Table(name = "BILL")
public class BillEntity {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BILL_SEQUENCE")
    @SequenceGenerator(name = "BILL_SEQUENCE", sequenceName = "BILL_SEQUENCE")
    private long id;

    @Column(name = "PRICE")
    private float price;

    @Column(name = "CAR_IMMATRICULATION")
    private String carImmatriculation;

    @Column(name = "PARKING_ID")
    private long parkingId;

    @Column(name = "ARRIVAL_TIME")
    private long arrivalTime;

    @Column(name = "DEPARTURE_TIME")
    private long departureTime;

    @Column(name = "SLOT_TYPE")
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    public BillEntity(Car car){
        this.price = car.getBill();
        this.carImmatriculation = car.getImmatriculation();
        this.parkingId = car.getParkingId();
        this.arrivalTime = car.getArrivalTime();
        this.departureTime = car.getDepartureTime();
        this.slotType = car.getSlotType();
    }
}
