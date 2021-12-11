package com.inho.mvc2.domain.validator;


import com.inho.mvc2.web.model.form.item.ItemUpdateForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemUpdateObjectValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz) {
        // Item 클래스 이거나 그 자식 클래스들인지 판단
        return ItemUpdateForm.class.isAssignableFrom(clazz) ;
    }

    @Override
    public void validate(Object target, Errors errors) {

        ItemUpdateForm item = (ItemUpdateForm)target;

        // 복합룰 검증
        if ( item.getPrice() != null && item.getQuantity() != null ){
            int resultPrice = item.getPrice() * item.getQuantity();
            if ( resultPrice < 10000 ){
                errors.reject("totalPriceMin", new Object[]{resultPrice}, null);
            }
        }

    }
}
