package backend.service;

import backend.dto.ItemDto;

import java.util.List;

public interface ItemService {

    public ItemDto save(ItemDto itemDto, String managerUsername);

    public List<ItemDto> findAllByMenuId(Integer id, String managerUsername);

    public ItemDto findById(Integer id, String managerUsername);

    public ItemDto delete(Integer id, String managerUsername);
}
