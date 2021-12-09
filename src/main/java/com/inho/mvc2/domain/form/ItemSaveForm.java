package com.inho.mvc2.domain.form;

import com.inho.mvc2.domain.Item;
import com.inho.mvc2.domain.ItemType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemSaveForm {

    @NotBlank //빈값+공백만 허용안함
    private String itemName;

    @NotNull// null 허용안함
    @Range(min = 1000, max=1000000) // hibernate validator 전용
    private Integer price;

    @NotNull
    private Integer quantity;

    private Boolean open;           //판매 여부
    private List<String> regions;   //등록 지역
    private ItemType itemType;      //상품 종류
    private String deliveryCode;    //배송 방식


    public ItemSaveForm() {
    }

    public ItemSaveForm(Item item)
    {
        itemName    = item.getItemName();
        price       = item.getPrice();
        quantity    = item.getQuantity();
        open        = item.getOpen();
        regions     = item.getRegions();
        itemType    = item.getItemType();
        deliveryCode= item.getDeliveryCode();
    }
}
