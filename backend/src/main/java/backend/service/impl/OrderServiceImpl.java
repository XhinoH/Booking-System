package backend.service.impl;

import backend.dto.OrderDto;
import backend.dto.OrderedItemDto;
import backend.exception.CustomRequestException;
import backend.exception.InvalidRequestException;
import backend.model.*;
import backend.repository.*;
import backend.service.OrderService;
import backend.service.OrderedItemService;
import backend.util.DtoConversion;
import backend.util.GetterUtil;
import backend.util.ValidationUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderedItemRepository orderedItemRepository;
    private final OrderedItemService orderedItemService;
    private final MenuRepository menuRepository;
    private final GetterUtil getterUtil;
    private final ValidationUtil validationUtil;

    private DtoConversion dtoConversion = new DtoConversion();

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, UserRepository userRepository, OrderedItemRepository orderedItemRepository, OrderedItemService orderedItemService, MenuRepository menuRepository, GetterUtil getterUtil, ValidationUtil validationUtil) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.orderedItemService = orderedItemService;
        this.menuRepository = menuRepository;
        this.getterUtil = getterUtil;
        this.validationUtil = validationUtil;
    }

    // Saving a new order
    @Override
    public OrderDto save(OrderDto orderDto, String clientUsername) {
        Order order;
        OrderDetail orderDetail;
        User user;

        // Updating the order if the orderDto has an id
        if (orderDto.getId() != null) {
            Optional<Order> orderOptional = orderRepository.findByIdAndIsDeleted(orderDto.getId(), false);
            if (orderOptional.isPresent()) {
                if (orderOptional.get().getOrderDetail().getStatus().equalsIgnoreCase("created")) {
                    order = orderOptional.get();
                } else {
                    logger.error("Order not found");
                    throw new CustomRequestException("Order can not be updated");
                }
            } else {
                throw new NullPointerException("Order not found");
            }
            Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findByOrderId(orderDto.getId());
            if (orderDetailOptional.isPresent()) {
                orderDetail = orderDetailOptional.get();
            } else {
                orderDetail = new OrderDetail();
            }

        } else {
            order = new Order();
            orderDetail = new OrderDetail();
        }

        // Adding the menu to order
        if (orderDto.getMenuId() != null) {
            Optional<Menu> menuOptional = menuRepository.findByIdAndIsDeleted(orderDto.getMenuId(), false);
            if (menuOptional.isPresent() && validationUtil.isMenuActive(menuOptional.get())) {
                Menu menu = menuOptional.get();
                order.setMenu(menu);
            } else {
                throw new NullPointerException("Menu not found");
            }
        } else {
            throw new InvalidRequestException("Menu Id is invalid");
        }

        order.setOrderDate(LocalDateTime.now());

        Optional<User> userOptional = userRepository.findByUsernameAndIsDeleted(clientUsername, false);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            order.setUser(user);
        } else {
            throw new NullPointerException("User not found");
        }

        // Adding the order details and setting the order status to created
        if (orderDto.getOrderDetail() != null) {
            orderDetail.setStatus("CREATED");
            if (orderDto.getOrderDetail().getPaymentMethod() != null) {
                orderDetail.setPaymentMethod(orderDto.getOrderDetail().getPaymentMethod());
            } else {
                throw new InvalidRequestException("Payment Method is invalid");
            }
            if (orderDto.getOrderDetail().getTransportFee() != null) {
                orderDetail.setTransportFee(orderDto.getOrderDetail().getTransportFee());
            } else {
                throw new InvalidRequestException("Transport fee is invalid");
            }
            orderDetail.setOrder(order);
            order.setOrderDetail(orderDetail);
        } else {
            throw new InvalidRequestException("Order Detail is invalid");
        }

//        if (orderDto.getOrderedItemList() != null) {
//            for (OrderedItemDto orderedItemDto : orderDto.getOrderedItemList()){
//                orderedItemService.save(orderedItemDto);
//            }
//            order.setOrderedItemList(orderDto.getOrderedItemList()
//                    .stream().map(orderedItemDto -> {
//                        Optional<OrderedItem> orderedItemOptional = orderedItemRepository.findById(orderedItemDto.getId());
//                        if (orderedItemOptional.isPresent() &&
//                                orderedItemOptional.get().getOrder().getMenu().getId() == orderDto.getMenuId()) {
//                            return orderedItemOptional.get();
//                        } else {
//                            throw new NullPointerException("Ordered Item not found");
//                        }
//                    }).collect(Collectors.toList()));
//        }


        logger.info("Order saved");
        return dtoConversion.convertOrder(orderRepository.save(order));
    }

    // Finding all the orders of a restaurant by it's manager username
    @Override
    public List<OrderDto> findAllByManagerUsername(String managerUsername) {
        return orderRepository.findAllByMenu_Restaurant_IdAndIsDeleted(getterUtil.getRestaurantId(managerUsername), false)
                .stream().map(dtoConversion::convertOrder).collect(Collectors.toList());
    }

    // Finding all the orders of a restaurant by their status
    @Override
    public List<OrderDto> findAllByStatus(String managerUsername, String orderStatus) {
        return orderRepository
                .findAllByMenu_Restaurant_IdAndOrderDetail_StatusAndIsDeleted(getterUtil.getRestaurantId(managerUsername), orderStatus, false)
                .stream().map(dtoConversion::convertOrder).collect(Collectors.toList());
    }

    // Finding all the orders of a client
    @Override
    public List<OrderDto> findAllByClientUsername(String clientUsername) {
        return orderRepository.findAllByUser_UsernameAndIsDeletedOrderByOrderDateDesc(clientUsername, false)
                .stream().map(dtoConversion::convertOrder).collect(Collectors.toList());
    }

    // MANAGER: Finding an order by id
    @Override
    public OrderDto findById(Integer orderId, String managerUsername) {
        Optional<Order> orderOptional = orderRepository
                .findByIdAndMenu_Restaurant_IdAndIsDeleted(orderId, getterUtil.getRestaurantId(managerUsername), false);
        if (orderOptional.isPresent()){
            return dtoConversion.convertOrder(orderOptional.get());
        } else {
            throw new NullPointerException("Order not found");
        }
    }

    // CLIENT: Finding an order by id
    @Override
    public OrderDto findByIdAndClientUsername(Integer orderId, String clientUsername) {
        Optional<Order> orderOptional = orderRepository
                .findByIdAndUser_UsernameAndIsDeleted(orderId, clientUsername, false);
        if (orderOptional.isPresent()){
            return dtoConversion.convertOrder(orderOptional.get());
        } else {
            throw new NullPointerException("Order not found");
        }
    }

    // Changing the status of an order
    @Override
    public OrderDto changeStatus(Integer orderId, String status, String managerUsername) {
        Optional<Order> orderOptional = orderRepository.findByIdAndIsDeleted(orderId, false);
        if (orderOptional.isPresent() && orderOptional.get().getMenu().getRestaurant().getId() ==
                getterUtil.getRestaurantId(managerUsername)) {

            Order order = orderOptional.get();

            if (status.equalsIgnoreCase("approved") &&
                    order.getOrderDetail().getStatus().equalsIgnoreCase("created")) {
                order.getOrderDetail().setStatus(status.toUpperCase());
            } else if (status.equalsIgnoreCase("rejected") &&
                    order.getOrderDetail().getStatus().equalsIgnoreCase("created")) {
                order.getOrderDetail().setStatus(status.toUpperCase());
            } else if (status.equalsIgnoreCase("prepared") &&
                    order.getOrderDetail().getStatus().equalsIgnoreCase("approved")) {
                order.getOrderDetail().setStatus(status.toUpperCase());
            } else if (status.equalsIgnoreCase("waiting_for_delivery") &&
                    order.getOrderDetail().getStatus().equalsIgnoreCase("prepared")) {
                order.getOrderDetail().setStatus(status.toUpperCase());
            } else if (status.equalsIgnoreCase("delivered") &&
                    order.getOrderDetail().getStatus().equalsIgnoreCase("waiting_for_delivery")) {
                order.getOrderDetail().setStatus(status.toUpperCase());
            } else {
                throw new CustomRequestException("You can not set this status");
            }

            logger.info("Changed order status of order with id: " + orderId + " to " + status);
            return dtoConversion.convertOrder(orderRepository.save(order));

        } else {
            throw new NullPointerException("Order not found");
        }
    }

    // Deleting an order
    @Override
    public OrderDto delete(Integer id, String managerUsername) {
        Optional<Order> orderOptional = orderRepository
                .findByIdAndMenu_Restaurant_IdAndIsDeleted(id, getterUtil.getRestaurantId(managerUsername), false);
        if (orderOptional.isPresent()){
            Order order = orderOptional.get();
            order.setDeleted(true);
            logger.info("Deleted order with id: " + id);
            return dtoConversion.convertOrder(orderRepository.save(order));
        } else {
            logger.error("Order not found");
            throw new NullPointerException("Order not found");
        }
    }
}
