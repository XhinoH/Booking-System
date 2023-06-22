package webapp.controller;

import backend.dto.RestaurantDto;
import backend.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> save(@RequestBody RestaurantDto restaurantDto){

        return ResponseEntity.ok(restaurantService.save(restaurantDto));

    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody RestaurantDto restaurantDto){

        return ResponseEntity.ok(restaurantService.save(restaurantDto));

    }


    @GetMapping
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(restaurantService.findAll());

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Integer id){

        return ResponseEntity.ok(restaurantService.delete(id));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Integer restaurantId){

        return ResponseEntity.ok(restaurantService.findById(restaurantId));

    }

    @GetMapping("/{id}/active")
    public ResponseEntity<?> showActiveMenu(@PathVariable(name = "id") Integer restaurantId){

        return ResponseEntity.ok(restaurantService.showActiveMenu(restaurantId));

    }
}
