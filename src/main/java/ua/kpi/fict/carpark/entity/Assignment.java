package ua.kpi.fict.carpark.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assignments_generator")
    @SequenceGenerator(name = "assignments_generator", sequenceName = "assignments_sequence")
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id", unique = true, nullable = false)
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "bus_id", unique = true, nullable = false)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
}
