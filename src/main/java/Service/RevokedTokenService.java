package Service;

import Entity.RevokedToken;
import Repository.RevokedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RevokedTokenService {
    private final RevokedRepository revokedRepository;

    public boolean addRevoked(RevokedToken revokedToken){
        RevokedToken saved = revokedRepository.save(revokedToken);
        return saved != null && saved.getId() != null;
    }

    public boolean findByToken(String token){
        return revokedRepository.findByToken(token).isPresent();
    }

}
