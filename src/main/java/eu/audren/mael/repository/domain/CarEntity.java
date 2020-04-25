package eu.audren.mael.repository.domain;

import eu.audren.mael.model.Car;
import eu.audren.mael.model.Parking;
import eu.audren.mael.model.SlotType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(of = "immatriculation")
@Entity(name = "CAR")
@Table(name = "CAR")
public class CarEntity implements Serializable {

    @Column(name = "IMMATRICULATION")
    private String immatriculation;

    @Column(name = "SLOT_TYPE")
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @Column(name = "ARRIVAL_TIME")
    private long arrivalTime;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAR_IMMATRICULATION")
    private ParkingEntity parkingUsed;

    public CarEntity(String immatriculation, SlotType slotType, long arrivalTime, ParkingEntity parkingUsed){
        this.immatriculation = immatriculation;
        this.slotType = slotType;
        this.arrivalTime = arrivalTime;
        this.parkingUsed = parkingUsed;

    }

    public CarEntity(Car car, ParkingEntity parkingEntity){
        this(car.getImmatriculation(), car.getSlotType(), car.getArrivalTime(), parkingEntity);
    }
}
