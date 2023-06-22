package backend.service;

import backend.dto.MenuDto;
import backend.dto.OrderDto;
import backend.dto.RestaurantDto;

import java.util.List;

public interface RestaurantService {

    public RestaurantDto save(RestaurantDto restaurantDto);
    public List<RestaurantDto> findAll();
    public RestaurantDto findById(Integer id);
    public MenuDto showActiveMenu(Integer restaurantId);
    public RestaurantDto delete(Integer id);
}
