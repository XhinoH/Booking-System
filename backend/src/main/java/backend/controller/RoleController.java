package backend.controller;

import backend.dto.RoleDto;
import backend.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> save(@RequestBody RoleDto roleDto){

        return ResponseEntity.ok(roleService.save(roleDto));

    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody RoleDto roleDto){

        return ResponseEntity.ok(roleService.save(roleDto));

    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(roleService.findAll());

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Integer id){

        return ResponseEntity.ok(roleService.findById(id));

    }


}
