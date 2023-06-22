package backend.util;

import backend.dto.*;
import backend.model.*;

import java.util.stream.Collectors;


public class DtoConversion {

    public UserDto convertUser(User user) {
        UserDto userDto = new UserDto();

        if (user.getId() != null) {
            userDto.setId(user.getId());
        }

        if (user.getUsername() != null){
            userDto.setUsername(user.getUsername());
        }

        if (user.getRoles() != null) {
            userDto.setRoles(user.getRoles().stream().map(this::convertRole).collect(Collectors.toSet()));
        }

        if (user.getUserDetail() != null) {
            userDto.setUserDetail(this.convertUserDetail(user.getUserDetail()));
        }

        if (user.getRestaurant() != null) {
            userDto.setRestaurantId(user.getRestaurant().getId());
        }

        return userDto;
    }

    public ClientDto convertClient(User user) {
        ClientDto clientDto = new ClientDto();

        if (user.getId() != null) {
            clientDto.setId(user.getId());
        }

        if (user.getUsername() != null){
            clientDto.setUsername(user.getUsername());
        }

        if (user.getUserDetail() != null) {
            clientDto.setUserDetail(this.convertUserDetail(user.getUserDetail()));
        }

        return clientDto;
    }

    public UserDetailDto convertUserDetail(UserDetail userDetail) {
        UserDetailDto userDetailDto = new UserDetailDto();
        if (userDetail.getId() != null){
            userDetailDto.setId(userDetail.getId());
        }

        if (userDetail.getFirstName() != null){
            userDetailDto.setFirstName(userDetail.getFirstName());
        }

        if (userDetail.getLastName() != null){
            userDetailDto.setLastName(userDetail.getLastName());
        }

        if (userDetail.getEmail() != null){
            userDetailDto.setEmail(userDetail.getEmail());
        }

        if (userDetail.getPhoneNumber() != null){
            userDetailDto.setPhoneNumber(userDetail.getPhoneNumber());
        }

        if (userDetail.getAddress() != null){
            userDetailDto.setAddress(userDetail.getAddress());
        }

        return userDetailDto;
    }

    public RoleDto convertRole(Role role) {
        RoleDto roleDto = new RoleDto();

        if (role.getId() != null){
            roleDto.setId(role.getId());
        }

        if (role.getName() != null){
            roleDto.setName(role.getName());
        }

        return roleDto;
    }

    public RestaurantDto convertRestaurant(Restaurant restaurant) {
        RestaurantDto restaurantDto = new RestaurantDto();

        if (restaurant.getId() != null){
            restaurantDto.setId(restaurant.getId());
        }

        if (restaurant.getName() != null){
            restaurantDto.setName(restaurant.getName());
        }

        if (restaurant.getManager() != null) {
            restaurantDto.setManagerUsername(restaurant.getManager().getUsername());
        }

        return restaurantDto;
    }

    public OrderedItemDto convertOrderedItem(OrderedItem orderedItem) {
        OrderedItemDto orderedItemDto = new OrderedItemDto();

        if (orderedItem.getId() != null){
            orderedItemDto.setId(orderedItem.getId());
        }

        if (orderedItem.getQuantity() != null){
            orderedItemDto.setQuantity(orderedItem.getQuantity());
        }

        if (orderedItem.getOrder() != null) {
            orderedItemDto.setOrderId(orderedItem.getOrder().getId());
        }

        if (orderedItem.getItem() != null) {
            orderedItemDto.setItemId(orderedItem.getItem().getId());
        }

        return orderedItemDto;
    }

    public OrderDto convertOrder(Order order) {
        OrderDto orderDto = new OrderDto();

        if (order.getId() != null) {
            orderDto.setId(order.getId());
        }

        if (order.getUser() != null) {
            orderDto.setUser(order.getUser().getUsername());
        }

        if (order.getOrderedItemList() != null) {
            orderDto.setOrderedItemList(order.getOrderedItemList()
                    .stream().map(this::convertOrderedItem)
                    .collect(Collectors.toList()));
        }

        if (order.getOrderDetail() != null) {
            orderDto.setOrderDetail(this.convertOrderDetail(order.getOrderDetail()));
        }

        if (order.getOrderDate() != null) {
            orderDto.setOrderDate(order.getOrderDate());
        }

        if (order.getMenu() != null) {
            orderDto.setMenuId(order.getMenu().getId());
        }

        return orderDto;
    }

    public OrderDetailDto convertOrderDetail(OrderDetail orderDetail) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();

        if (orderDetail.getId() != null){
            orderDetailDto.setId(orderDetail.getId());
        }

        if (orderDetail.getStatus() != null){
            orderDetailDto.setStatus(orderDetail.getStatus());
        }

        if (orderDetail.getTransportFee() != null){
            orderDetailDto.setTransportFee(orderDetail.getTransportFee());
        }

        if (orderDetail.getPaymentMethod() != null){
            orderDetailDto.setPaymentMethod(orderDetail.getPaymentMethod());
        }

        return orderDetailDto;
    }

    public MenuDto convertMenu(Menu menu) {
        MenuDto menuDto = new MenuDto();

        if (menu.getId() != null){
            menuDto.setId(menu.getId());
        }

        if (menu.getStartTime() != null){
            menuDto.setStartTime(menu.getStartTime());
        }

        if (menu.getEndTime() != null){
            menuDto.setEndTime(menu.getEndTime());
        }

        if (menu.getActive() != null){
            menuDto.setActive(menu.getActive());
        }

        if (menu.getName() != null){
            menuDto.setName(menu.getName());
        }

        if (menu.getItemList() != null) {
            menuDto.setItemList(menu.getItemList()
                    .stream().map(this::convertItem)
                    .collect(Collectors.toList()));
        }

        if (menu.getRestaurant().getId() != null) {
            menuDto.setRestaurantId(menu.getRestaurant().getId());
        }

        return menuDto;
    }

    public ItemDto convertItem(Item item) {
        ItemDto itemDto = new ItemDto();

        if (item.getId() != null){
            itemDto.setId(item.getId());
        }

        if (item.getName() != null){
            itemDto.setName(item.getName());
        }

        if (item.getPrice() != null){
            itemDto.setPrice(item.getPrice());
        }

        if (item.getMenu() != null){
            itemDto.setMenuId(item.getMenu().getId());
        }

        return itemDto;
    }
}
