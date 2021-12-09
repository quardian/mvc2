package com.inho.mvc2.domain.validator;


import com.inho.mvc2.domain.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class ItemObjectValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz) {
        // Item 클래스 이거나 그 자식 클래스들인지 판단
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item)target;

        // 복합룰 검증
        if ( item.getPrice() != null && item.getQuantity() != null ){
            int resultPrice = item.getPrice() * item.getQuantity();
            if ( resultPrice < 10000 ){
                errors.reject("totalPriceMin", new Object[]{resultPrice}, null);
            }
        }

    }
}
