package ua.kpi.fict.carpark.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_generator")
    @SequenceGenerator(name = "route_generator", sequenceName = "route_sequence")
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer number;

    @OneToMany(mappedBy = "route")
    private List<Bus> buses = new ArrayList<>();
}
