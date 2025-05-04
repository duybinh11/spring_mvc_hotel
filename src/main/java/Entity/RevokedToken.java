package Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "revoked_token")
public class RevokedToken extends AbstractEntity<Long>{
    @Column(unique = true, length = 500)
    private String token;
    private Date expiryDate;
}
