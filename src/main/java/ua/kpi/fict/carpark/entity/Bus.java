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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"id", "model", "capacity"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "buses_generator")
    @SequenceGenerator(name = "buses_generator", sequenceName = "buses_sequence")
    private Long id;

    private String model;

    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToOne
    @JoinColumn(name = "driver_id", unique = true)
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "assignment_id", unique = true)
    private Assignment assignment;
}
