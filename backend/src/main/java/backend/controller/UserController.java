package backend.controller;

import backend.model.dto.UserDto;
import backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService ) {
        this.userService = userService;

    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> save(@RequestBody UserDto userDto){

        return ResponseEntity.ok(userService.save(userDto));

    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody UserDto userDto){

        return ResponseEntity.ok(userService.save(userDto));

    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAllByRole(@RequestParam String roleName){

        return ResponseEntity.ok(userService.findAllByRole(roleName));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Integer id){

        return ResponseEntity.ok(userService.findById(id));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRoleToUser(@PathVariable(name = "id") Integer id, @RequestParam String roleName){

        return ResponseEntity.ok(userService.addRoleToUser(id,roleName));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Integer id){

        return ResponseEntity.ok(userService.delete(id));

    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(userService.findAll());

    }


}
