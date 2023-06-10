package com.zhi.delivery.dto;



import com.zhi.delivery.entity.OrderDetail;
import com.zhi.delivery.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
