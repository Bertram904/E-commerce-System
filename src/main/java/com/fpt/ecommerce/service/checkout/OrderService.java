package com.fpt.ecommerce.service.checkout;

import com.fpt.ecommerce.dto.request.OrderRequest;
import com.fpt.ecommerce.dto.response.OrderResponse;

public interface OrderService {
    public OrderResponse checkout(Long  memberId, OrderRequest orderRequest);

}
