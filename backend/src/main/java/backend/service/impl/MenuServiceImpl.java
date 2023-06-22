package backend.service.impl;

import backend.dto.MenuDto;
import backend.exception.InvalidRequestException;
import backend.model.Menu;
import backend.model.Restaurant;
import backend.repository.ItemRepository;
import backend.repository.MenuRepository;
import backend.repository.RestaurantRepository;
import backend.service.MenuService;
import backend.util.DtoConversion;
import backend.util.GetterUtil;
import backend.util.ValidationUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final MenuRepository menuRepository;
    private final ItemRepository itemRepository;
    private final RestaurantRepository restaurantRepository;
    private final GetterUtil getterUtil;
    private final ValidationUtil validationUtil;

    private DtoConversion dtoConversion = new DtoConversion();

    public MenuServiceImpl(MenuRepository menuRepository, ItemRepository itemRepository, RestaurantRepository restaurantRepository, GetterUtil getterUtil, ValidationUtil validationUtil) {
        this.menuRepository = menuRepository;
        this.itemRepository = itemRepository;
        this.restaurantRepository = restaurantRepository;
        this.getterUtil = getterUtil;
        this.validationUtil = validationUtil;
    }

    @Override
    public MenuDto save(MenuDto menuDto, String managerUsername) {

        Menu menu;
        Restaurant restaurant;
        Boolean isUpdating;

        if (menuDto.getId() != null) {
            Optional<Menu> menuOptional = menuRepository.findByIdAndIsDeleted(menuDto.getId(), false);
            if (menuOptional.isPresent()) {
                menu = menuOptional.get();
                isUpdating = true;
            } else {
                logger.error("Menu not found");
                throw new NullPointerException("Menu not found");
            }
        } else {
            menu = new Menu();
            isUpdating = false;
        }


        if ((menuDto.getStartTime() == null && isUpdating == false) || (menuDto.getEndTime() == null && isUpdating == false)){
            throw new InvalidRequestException("Start or end time is invalid");
        }else if (menuDto.getStartTime() != null && menuDto.getEndTime() != null){
            if (!validationUtil.menuReadyToSave(menuDto.getStartTime(), menuDto.getEndTime(), managerUsername)){
                throw new InvalidRequestException("Start time and end time collide with other menus time");
            }
            menu.setStartTime(menuDto.getStartTime());
            menu.setEndTime(menuDto.getEndTime());
        }

        if (validationUtil.isMenuActive(menu)){
            menu.setActive(true);
        }else {
            menu.setActive(false);
        }

        if (menuDto.getName() != null){
            menu.setName(menuDto.getName());
        }else {
            throw new InvalidRequestException("Menu name is invalid");
        }

        Optional<Restaurant> restaurantOptional = restaurantRepository
                .findByIdAndIsDeleted(getterUtil.getRestaurantId(managerUsername), false);
        if (restaurantOptional.isPresent()) {
            restaurant = restaurantOptional.get();
            menuRepository.save(menu);
            List<Menu> menuList;
            if (restaurant.getMenuList() != null){
                 menuList = restaurant.getMenuList();
            }else {
                menuList = new ArrayList<>();
            }
            menuList.add(menu);
            restaurant.setMenuList(menuList);
            restaurantRepository.save(restaurant);
            menu.setRestaurant(restaurant);
        } else {
            throw new NullPointerException("Restaurant not found");
        }

        logger.info("Saved menu with name: " + menu.getName() + " to restaurant: " + menu.getRestaurant().getName());
        return dtoConversion.convertMenu(menuRepository.save(menu));
    }

    @Override
    public List<MenuDto> findAllByManagerUsername(String managerUsername) {

        return menuRepository
                .findAllByRestaurantIdAndIsDeleted(getterUtil.getRestaurantId(managerUsername), false)
                .stream().map(dtoConversion::convertMenu).collect(Collectors.toList());

    }

    @Override
    public MenuDto findById(Integer id, String managerUsername) {

        Optional<Menu> menuOptional = menuRepository
                .findByIdAndRestaurantIdAndIsDeleted(id, getterUtil.getRestaurantId(managerUsername), false);
        if (menuOptional.isPresent()){
            Menu menu = menuOptional.get();
            return dtoConversion.convertMenu(menu);
        }else {
            throw new NullPointerException("Menu not found");
        }
    }

    @Override
    public MenuDto delete(Integer id, String managerUsername) {
        Optional<Menu> menuOptional = menuRepository
                .findByIdAndRestaurantIdAndIsDeleted(id, getterUtil.getRestaurantId(managerUsername), false);
        if (menuOptional.isPresent()){
            Menu menu = menuOptional.get();
            menu.setDeleted(true);

            logger.info("Deleted menu with id: " + id);
            return dtoConversion.convertMenu(menuRepository.save(menu));
        } else {
            logger.error("Menu not found");
            throw new NullPointerException("Menu not found");
        }
    }

}
