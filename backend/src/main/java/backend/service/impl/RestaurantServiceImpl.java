package backend.service.impl;

import backend.dto.MenuDto;
import backend.dto.RestaurantDto;
import backend.exception.CustomRequestException;
import backend.model.Menu;
import backend.model.Restaurant;
import backend.model.User;
import backend.repository.MenuRepository;
import backend.repository.RestaurantRepository;
import backend.repository.UserRepository;
import backend.service.RestaurantService;
import backend.util.DtoConversion;
import backend.util.ValidationUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final ValidationUtil validationUtil;
    private DtoConversion dtoConversion = new DtoConversion();

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, UserRepository userRepository, MenuRepository menuRepository, ValidationUtil validationUtil) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
        this.validationUtil = validationUtil;
    }

    // Saving a new restaurant
    @Override
    public RestaurantDto save(RestaurantDto restaurantDto) {

        Restaurant restaurant;

        // Updating the restaurant if restaurantDto has an id
        if (restaurantDto.getId() != null){
            Optional<Restaurant> restaurantOptional = restaurantRepository.findByIdAndIsDeleted(restaurantDto.getId(), false);
            if (restaurantOptional.isPresent()){
                restaurant = restaurantOptional.get();
            }
            else {
                logger.error("Restaurant not found");
                throw new NullPointerException("Restaurant not found");
            }
        }
        else {
            restaurant = new Restaurant();
        }

        if (restaurantDto.getName() != null){
            restaurant.setName(restaurantDto.getName());
        } else {
            throw new IllegalArgumentException("Restaurant name is invalid");
        }

        logger.info("Saved restaurant with name: " + restaurant.getName());
        return dtoConversion.convertRestaurant(restaurantRepository.save(restaurant));
    }

    // Finding all the restaurants
    @Override
    public List<RestaurantDto> findAll() {
        return restaurantRepository.findAllByIsDeleted(false).stream()
                .map(dtoConversion::convertRestaurant).collect(Collectors.toList());
    }

    // Finding a restaurant by id
    @Override
    public RestaurantDto findById(Integer restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByIdAndIsDeleted(restaurantId, false);
        if (restaurantOptional.isPresent()){
            return dtoConversion.convertRestaurant(restaurantOptional.get());
        } else {
            throw new NullPointerException("Restaurant not found");
        }
    }

    // Showing the active menu of a restaurant
    @Override
    public MenuDto showActiveMenu(Integer restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByIdAndIsDeleted(restaurantId, false);
        if (restaurantOptional.isEmpty()){
            throw new NullPointerException("Restaurant not found");
        }
        Restaurant restaurant = restaurantOptional.get();
        List<Menu> menuList = restaurant.getMenuList();
        if (menuList == null){
            throw new CustomRequestException("Restaurant does not have any menu");
        }

        for (Menu menu : menuList) {
            if (validationUtil.isMenuActive(menu)){
                menu.setActive(true);
                menuRepository.save(menu);
            } else {
                menu.setActive(false);
                menuRepository.save(menu);
            }
        }

        Optional<Menu> menuOptional = menuRepository.findByIsActiveAndRestaurant_IdAndIsDeleted(true, restaurantId, false);
        if (menuOptional.isPresent()){
            return dtoConversion.convertMenu(menuOptional.get());
        } else {
            throw new NullPointerException("Active menu not found");
        }
    }

    // Deleting a restaurant
    @Override
    public RestaurantDto delete(Integer restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByIdAndIsDeleted(restaurantId, false);
        if (restaurantOptional.isPresent()){
            Restaurant restaurant = restaurantOptional.get();
            restaurant.setDeleted(true);
            logger.info("Deleted restaurant with name: " + restaurant.getName());
            return dtoConversion.convertRestaurant(restaurantRepository.save(restaurant));
        } else {
            logger.info("Restaurant with id: " + restaurantId + " not found");
            throw new NullPointerException("Restaurant not found");
        }
    }
}
