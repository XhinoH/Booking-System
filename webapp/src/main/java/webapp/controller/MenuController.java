package webapp.controller;

import backend.dto.MenuDto;
import backend.service.MenuService;
import backend.util.GetterUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;
    private final GetterUtil getterUtil;

    public MenuController(MenuService menuService, GetterUtil getterUtil) {
        this.menuService = menuService;
        this.getterUtil = getterUtil;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> save(@RequestBody MenuDto menuDto, HttpServletRequest request){

        return ResponseEntity.ok(menuService.save(menuDto, getterUtil.getUsernameFromRequest(request)));

    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> update(@RequestBody MenuDto menuDto, HttpServletRequest request){

        return ResponseEntity.ok(menuService.save(menuDto, getterUtil.getUsernameFromRequest(request)));

    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> findAll(HttpServletRequest request){

        return ResponseEntity.ok(menuService.findAllByManagerUsername(getterUtil.getUsernameFromRequest(request)));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> findById(@PathVariable(value = "id") Integer id, HttpServletRequest request){

        return ResponseEntity.ok(menuService.findById(id, getterUtil.getUsernameFromRequest(request)));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id, HttpServletRequest request){

        return ResponseEntity.ok(menuService.delete(id, getterUtil.getUsernameFromRequest(request)));

    }
}
