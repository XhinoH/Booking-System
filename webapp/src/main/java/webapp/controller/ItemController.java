package webapp.controller;

import backend.dto.ItemDto;
import backend.exception.InvalidRequestException;
import backend.security.JwtUtil;
import backend.service.ItemService;
import backend.util.GetterUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final JwtUtil jwtUtil;
    private final GetterUtil getterUtil;

    public ItemController(ItemService itemService, JwtUtil jwtUtil, GetterUtil getterUtil) {
        this.itemService = itemService;
        this.jwtUtil = jwtUtil;
        this.getterUtil = getterUtil;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> save(@RequestBody ItemDto itemDto, HttpServletRequest request){
        return ResponseEntity.ok(itemService.save(itemDto, getterUtil.getUsernameFromRequest(request)));
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> update(@RequestBody ItemDto itemDto, HttpServletRequest request){
        return ResponseEntity.ok(itemService.save(itemDto, getterUtil.getUsernameFromRequest(request)));
    }

    @GetMapping
    public ResponseEntity<?> findAllByMenuId(@Nullable @RequestParam Integer menuId, HttpServletRequest request){
        if (menuId != null){
                return ResponseEntity.ok(itemService.findAllByMenuId(menuId, getterUtil.getUsernameFromRequest(request)));
        }else {
            throw new InvalidRequestException("Menu Id is invalid");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Integer itemId, HttpServletRequest request){

        return ResponseEntity.ok(itemService.findById(itemId, getterUtil.getUsernameFromRequest(request)));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Integer itemId, HttpServletRequest request){

        return ResponseEntity.ok(itemService.delete(itemId, getterUtil.getUsernameFromRequest(request)));

    }

}
