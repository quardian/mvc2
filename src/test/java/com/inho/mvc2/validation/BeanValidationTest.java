package com.inho.mvc2.validation;


import com.inho.mvc2.domain.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {
    @Test
    void beanValidation(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setItemName("");
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> validations = validator.validate(item);
        for (ConstraintViolation<Item> validation : validations) {
            System.out.println("validation = " + validation);
            System.out.println("validation.getMessage() = " + validation.getMessage());
        }
    }
}
