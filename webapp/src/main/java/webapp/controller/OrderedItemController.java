package webapp.controller;


import backend.dto.OrderedItemDto;
import backend.service.OrderedItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordereditems")
public class OrderedItemController {

    private final OrderedItemService orderedItemService;

    public OrderedItemController(OrderedItemService orderedItemService) {
        this.orderedItemService = orderedItemService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> save(@RequestBody OrderedItemDto orderedItemDto){

        return ResponseEntity.ok(orderedItemService.save(orderedItemDto));

    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> update(@RequestBody OrderedItemDto orderedItemDto){

        return ResponseEntity.ok(orderedItemService.save(orderedItemDto));

    }
}
