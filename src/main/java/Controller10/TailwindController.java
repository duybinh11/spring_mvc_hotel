package Controller10;

import Entity.TailwindEntity;
import Service.TailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tailwind")
@AllArgsConstructor
public class TailwindController {
    private final TailService tailService;

    @PostMapping
    void addTailwind(@RequestBody TailwindEntity tailwindEntity){
        tailService.addTailwind(tailwindEntity);
    }
}
