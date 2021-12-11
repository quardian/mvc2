package com.inho.mvc2.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class Item
{
    //@NotNull(groups= U.class)
    private Long id;


    //@NotBlank(groups={C.class, U.class}) //빈값+공백만 허용안함
    private String itemName;

    //@NotNull(groups={C.class, U.class}) // null 허용안함
    //@Range(min = 1000, max=1000000) // hibernate validator 전용
    private Integer price;

    //@NotNull(groups={C.class, U.class})
    //@Max( value=9999, groups={C.class} )
    private Integer quantity;

    private Boolean open;           //판매 여부
    private List<String> regions;   //등록 지역
    private ItemType itemType;      //상품 종류
    private String deliveryCode;    //배송 방식

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
