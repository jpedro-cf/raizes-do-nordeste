package raizes.nordeste.app.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "units")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Unit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
    private List<ProductUnit> productUnits = new ArrayList<>();
}
