package com.inho.mvc2.domain.validator;


import com.inho.mvc2.domain.model.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class ItemValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz) {
        // Item 클래스 이거나 그 자식 클래스들인지 판단
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item)target;

        // 특정필드 검증 로직
        if ( StringUtils.isEmpty(item.getItemName()) ){
            // 규칙 = "requried" + ".' + objectName + "." + field
            // means = new String[] {"required.item.itemName", "required"}
            // 코드 수정없이, 메시지만 수정해서 좀더 상세한 메시지에 접근 가능하다.
            errors.rejectValue("itemName", "required");

        }
        if ( item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000 ){
            errors.rejectValue("price", "range", new Object[]{"1,000", "1,000,000"}, null);
        }
        if ( item.getQuantity() == null || item.getQuantity() >= 9999){
            errors.rejectValue("quantity", "max");
        }

        // 복합룰 검증
        if ( item.getPrice() != null && item.getQuantity() != null ){
            int resultPrice = item.getPrice() * item.getQuantity();
            if ( resultPrice < 10000 ){
                errors.reject("totalPriceMin", new Object[]{resultPrice}, null);
            }
        }

    }
}
