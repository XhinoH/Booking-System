package backend.service.impl;

import backend.dto.ItemDto;
import backend.dto.MenuDto;
import backend.exception.CustomRequestException;
import backend.exception.InvalidRequestException;
import backend.model.Item;
import backend.model.Menu;
import backend.repository.ItemRepository;
import backend.repository.MenuRepository;
import backend.service.ItemService;
import backend.service.MenuService;
import backend.util.DtoConversion;
import backend.util.GetterUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final ItemRepository itemRepository;
    private final MenuRepository menuRepository;
    private final MenuService menuService;
    private final GetterUtil getterUtil;

    private DtoConversion dtoConversion = new DtoConversion();

    public ItemServiceImpl(ItemRepository itemRepository, MenuRepository menuRepository, MenuService menuService, GetterUtil getterUtil) {
        this.itemRepository = itemRepository;
        this.menuRepository = menuRepository;
        this.menuService = menuService;
        this.getterUtil = getterUtil;
    }

    @Override
    public ItemDto save(ItemDto itemDto, String managerUsername) {

        Item item;
        Menu menu;


        if (itemDto.getId() != null){
            Optional<Item> itemOptional = itemRepository.findByIdAndIsDeleted(itemDto.getId(), false);
            if (itemOptional.isPresent()){
                item = itemOptional.get();
            } else {
                logger.error("Item not found");
                throw new NullPointerException("Item not found");
            }
        }
        else {
            item = new Item();
        }

        if (itemDto.getName() != null){
            item.setName(itemDto.getName());
        } else {
            throw new InvalidRequestException("Item name is invalid");
        }

        if (itemDto.getPrice() != null){
            item.setPrice(itemDto.getPrice());
        } else {
            throw new InvalidRequestException("Item price is invalid");
        }

        if(itemDto.getMenuId() != null){
            Optional<Menu> menuOptional = menuRepository
                    .findByIdAndRestaurantIdAndIsDeleted(itemDto.getMenuId(), getterUtil.getRestaurantId(managerUsername), false);
            if (menuOptional.isPresent()){
                menu = menuOptional.get();
                itemRepository.save(item);
                List<Item> itemList;
                if (menu.getItemList() != null){
                    itemList = menu.getItemList();
                } else {
                    itemList = new ArrayList<>();
                }
                itemList.add(item);
                menu.setItemList(itemList);
                item.setMenu(menu);
            }else {
                logger.error("Menu not found");
                throw new NullPointerException("Menu not found");
            }
        }else {
            throw new InvalidRequestException("Menu is invalid");
        }

        logger.info("Saved item " + item.getName() + " in menu " + item.getMenu().getName());
        return dtoConversion.convertItem(itemRepository.save(item));
    }

    @Override
    public List<ItemDto> findAllByMenuId(Integer id, String managerUsername) {

        List<MenuDto> menuDtoList = menuService.findAllByManagerUsername(managerUsername);
        for (MenuDto menuDto : menuDtoList){
            if (id == menuDto.getId()){
                return itemRepository.findAllByMenu_IdAndIsDeleted(id, false).stream()
                        .map(dtoConversion::convertItem).collect(Collectors.toList());
            }
        }
        throw new CustomRequestException("Menu not found");

    }

    @Override
    public ItemDto findById(Integer itemId, String managerUsername) {
        Optional<Item> itemOptional = itemRepository
                .findByIdAndMenu_Restaurant_IdAndIsDeleted(itemId, getterUtil.getRestaurantId(managerUsername), false);
        if (itemOptional.isPresent()){
            return dtoConversion.convertItem(itemOptional.get());
        } else {
            throw new NullPointerException("Item not found");
        }
    }

    @Override
    public ItemDto delete(Integer id, String managerUsername) {
        Optional<Item> itemOptional = itemRepository
                .findByIdAndMenu_Restaurant_IdAndIsDeleted(id, getterUtil.getRestaurantId(managerUsername), false);
        if (itemOptional.isPresent()){
            Item item = itemOptional.get();
            item.setDeleted(true);

            logger.info("Deleted item with id:" + id);
            return dtoConversion.convertItem(itemRepository.save(item));
        } else {
            logger.error("Item not found");
            throw new NullPointerException("Item not found");
        }
    }

}
