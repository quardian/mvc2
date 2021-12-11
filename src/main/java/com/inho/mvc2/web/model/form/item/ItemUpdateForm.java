package com.inho.mvc2.web.model.form.item;

import com.inho.mvc2.domain.model.Item;
import com.inho.mvc2.domain.model.ItemType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank //빈값+공백만 허용안함
    private String itemName;

    @NotNull// null 허용안함
    @Range(min = 1000, max=1000000) // hibernate validator 전용
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    private Boolean open;           //판매 여부
    private List<String> regions;   //등록 지역
    private ItemType itemType;      //상품 종류
    private String deliveryCode;    //배송 방식


    public ItemUpdateForm() {
    }

    public ItemUpdateForm(Item item)
    {
        id          = item.getId();
        itemName    = item.getItemName();
        price       = item.getPrice();
        quantity    = item.getQuantity();
        open        = item.getOpen();
        regions     = item.getRegions();
        itemType    = item.getItemType();
        deliveryCode= item.getDeliveryCode();
    }

    public Item asItem()
    {
        Item item = new Item();
        item.setId(id);
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setOpen(open);
        item.setRegions(regions);
        item.setItemType(itemType);
        item.setDeliveryCode(deliveryCode);
        return item;
    }
}
