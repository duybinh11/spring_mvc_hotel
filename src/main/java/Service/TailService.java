package Service;

import Entity.TailwindEntity;
import Repository.TailwindRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TailService {
    private final TailwindRepository tailwindRepository;

    public void addTailwind(TailwindEntity tailwindEntity){
        tailwindRepository.save(tailwindEntity);
    }
}
