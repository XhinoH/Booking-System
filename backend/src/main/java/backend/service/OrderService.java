package backend.service;

import backend.dto.OrderDto;

import java.util.List;

public interface OrderService {

    public OrderDto save(OrderDto orderDto, String clientUsername);
    public List<OrderDto> findAllByManagerUsername(String managerUsername);
    public List<OrderDto> findAllByStatus(String managerUsername, String orderStatus);
    public List<OrderDto> findAllByClientUsername(String clientUsername);
    public OrderDto findById(Integer orderId, String managerUsername);
    public OrderDto findByIdAndClientUsername(Integer orderId, String clientUsername);
    public OrderDto changeStatus(Integer orderId, String status, String managerUsername);
    public OrderDto delete(Integer id, String managerUsername);
}
