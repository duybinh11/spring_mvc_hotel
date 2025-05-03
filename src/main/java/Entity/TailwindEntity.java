package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tailwind_entity")
public class TailwindEntity extends AbstractEntity<Long>{
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
}
