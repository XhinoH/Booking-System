package backend.service.impl;

import backend.dto.OrderedItemDto;
import backend.exception.CustomRequestException;
import backend.exception.InvalidRequestException;
import backend.model.Item;
import backend.model.Order;
import backend.model.OrderedItem;
import backend.repository.ItemRepository;
import backend.repository.OrderRepository;
import backend.repository.OrderedItemRepository;
import backend.service.OrderedItemService;
import backend.util.DtoConversion;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OrderedItemServiceImpl implements OrderedItemService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final OrderedItemRepository orderedItemRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private DtoConversion dtoConversion = new DtoConversion();

    public OrderedItemServiceImpl(OrderedItemRepository orderedItemRepository, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.orderedItemRepository = orderedItemRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderedItemDto save(OrderedItemDto orderedItemDto) {
        OrderedItem orderedItem;
        Item item;
        Order order;

        if (orderedItemDto.getId() != null){
            Optional<OrderedItem> orderedItemOptional = orderedItemRepository.findById(orderedItemDto.getId());
            if (orderedItemOptional.isPresent()){
                orderedItem = orderedItemOptional.get();
            }else {
                logger.error("Ordered Item not found");
                throw new NullPointerException("Ordered item not found");
            }
        }else {
            orderedItem = new OrderedItem();
        }

        if (orderedItemDto.getQuantity() != null){
            orderedItem.setQuantity(orderedItemDto.getQuantity());
        }else {
            throw new InvalidRequestException("Quantity is invalid");
        }

        if (orderedItemDto.getItemId() != null && orderedItemDto.getOrderId() != null){
            Optional<Item> itemOptional = itemRepository.findByIdAndIsDeleted(orderedItemDto.getItemId(), false);
            Optional<Order> orderOptional = orderRepository.findByIdAndIsDeleted(orderedItemDto.getOrderId(), false);
            if (itemOptional.isPresent() && orderOptional.isPresent() &&
                    itemOptional.get().getMenu().getId() == orderOptional.get().getMenu().getId()){
                item = itemOptional.get();
                order = orderOptional.get();

                if (!order.getOrderDetail().getStatus().equalsIgnoreCase("created")){
                    throw new CustomRequestException("You can not add items in this order");
                }

                orderedItemRepository.save(orderedItem);
                List<OrderedItem> orderedItemList = item.getOrderedItemList();
                orderedItemList.add(orderedItem);
                item.setOrderedItemList(orderedItemList);
                itemRepository.save(item);
                orderedItem.setItem(item);

                List<OrderedItem> orderedItemList1 = order.getOrderedItemList();
                orderedItemList1.add(orderedItem);
                order.setOrderedItemList(orderedItemList1);
                orderRepository.save(order);
                orderedItem.setOrder(order);
            }else {
                throw new NullPointerException("Item or Order not found");
            }
        }else {
            throw new InvalidRequestException("Item id or Order id is invalid");
        }

        logger.info("Added item " + orderedItem.getItem().getName() + " to order with id: " + orderedItem.getOrder().getId());
        return dtoConversion.convertOrderedItem(orderedItemRepository.save(orderedItem));
    }


}
