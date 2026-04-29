package org.example.pojo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OrderResponse {
    public ArrayList<String> orders;
    public ArrayList<String> productOrderId;
    public String message;
}
