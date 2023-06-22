package backend.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    private Integer id;
    private LocalDateTime orderDate;
    private String user;
    private Integer menuId;
    private OrderDetailDto orderDetail;
    private List<OrderedItemDto> orderedItemList = new ArrayList<>();


    ///////////////////////////////////////////////////////////////////////
    ///////////////////////// GETTERS AND SETTERS /////////////////////////
    ///////////////////////////////////////////////////////////////////////


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public OrderDetailDto getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetailDto orderDetail) {
        this.orderDetail = orderDetail;
    }

    public List<OrderedItemDto> getOrderedItemList() {
        return orderedItemList;
    }

    public void setOrderedItemList(List<OrderedItemDto> orderedItemList) {
        this.orderedItemList = orderedItemList;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
