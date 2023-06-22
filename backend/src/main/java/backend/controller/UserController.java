package backend.controller;

import backend.dto.ClientDto;
import backend.dto.UserDto;
import backend.service.UserService;
import backend.util.GetterUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final GetterUtil getterUtil;

    public UserController(UserService userService, GetterUtil getterUtil) {
        this.userService = userService;
        this.getterUtil = getterUtil;
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

    @PutMapping("/clients")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> updateClient(@RequestBody ClientDto clientDto, HttpServletRequest request){

        return ResponseEntity.ok(userService.saveClient(clientDto, getterUtil.getUsernameFromRequest(request)));

    }


}
