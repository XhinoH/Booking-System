package webapp.controller;

import backend.dto.OrderDto;
import backend.service.OrderService;
import backend.util.GetterUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final GetterUtil getterUtil;

    public OrderController(OrderService orderService, GetterUtil getterUtil) {
        this.orderService = orderService;
        this.getterUtil = getterUtil;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> save(@RequestBody OrderDto orderDto, HttpServletRequest request){

        return ResponseEntity.ok(orderService.save(orderDto, getterUtil.getUsernameFromRequest(request)));

    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> update(@RequestBody OrderDto orderDto, HttpServletRequest request){

        return ResponseEntity.ok(orderService.save(orderDto, getterUtil.getUsernameFromRequest(request)));

    }

    @PutMapping("/{id}/{status}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> changeStatus(@PathVariable(value = "id") Integer id, @PathVariable(value = "status") String status, HttpServletRequest request){

        return ResponseEntity.ok(orderService.changeStatus(id, status, getterUtil.getUsernameFromRequest(request)));

    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> findAll(HttpServletRequest request){

        return ResponseEntity.ok(orderService.findAllByManagerUsername(getterUtil.getUsernameFromRequest(request)));

    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> findAllByStatus(@PathVariable(name = "status") String orderStatus,HttpServletRequest request){

        return ResponseEntity.ok(orderService.findAllByStatus(getterUtil.getUsernameFromRequest(request), orderStatus));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Integer orderId,HttpServletRequest request){

        return ResponseEntity.ok(orderService.findById(orderId, getterUtil.getUsernameFromRequest(request)));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Integer orderId,HttpServletRequest request){

        return ResponseEntity.ok(orderService.delete(orderId, getterUtil.getUsernameFromRequest(request)));

    }

    @GetMapping("/clients")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> findAllByClientUsername(HttpServletRequest request){

        return ResponseEntity.ok(orderService.findAllByClientUsername(getterUtil.getUsernameFromRequest(request)));

    }

    @GetMapping("/clients/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> findByIdAndClientUsername(@PathVariable(name = "id") Integer orderId,HttpServletRequest request){

        return ResponseEntity.ok(orderService.findByIdAndClientUsername(orderId, getterUtil.getUsernameFromRequest(request)));

    }
}
