package backend.service;

import backend.dto.MenuDto;

import java.util.List;

public interface MenuService {

    public MenuDto save(MenuDto menuDto, String managerUsername);

    public List<MenuDto> findAllByManagerUsername(String managerUsername);

    public MenuDto findById(Integer id, String managerUsername);

    public MenuDto delete(Integer id, String managerUsername);
}
