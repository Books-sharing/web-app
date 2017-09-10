package com.ninjabooks.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QueueDto implements Serializable
{
    private static final long serialVersionUID = 8784779997262803415L;

    private String orderDate;

    public QueueDto() {
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate.toString();
    }
}