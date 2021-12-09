package com.inho.mvc2.validation;

import com.inho.mvc2.Repository.ItemRepository;
import com.inho.mvc2.domain.*;
import com.inho.mvc2.domain.crud.C;
import com.inho.mvc2.domain.crud.U;
import com.inho.mvc2.domain.validator.ItemObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor


@RequestMapping(value="/validation/v3/items")
public class ValidationV3Conroller
{
    private final ItemRepository itemRepository;
    private final LocaleResolver localeResolver;
    private final ItemObjectValidator itemObjectValidator;
    /**
     * @ModelAttribute의 특별한 사용법
     * 이 컨트롤러 호출시, 무조건 모델에 regions로 할당된다.
     * @return
     */
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    /**
     * 이 컨트롤러 호출시, 무조건 모델에 itemTypes로 할당된다.
     * @return
     */
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    /**
     * 이 컨트롤러 호출시, 무조건 모델에 deliveryCodes로 할당된다.
     * @return
     */
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }



    @GetMapping("/items")
    public String items(Model model) {

        new Builder(model)
                .setItemTypes("itemTypes1")
                .setDeliveryCodes("deliveryCodes1")
                .setRegions("regions1")
                .set("user1", "value1")
                .set("user2", "value2")
                .build();

        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        Locale locale = localeResolver.resolveLocale(request);
        if ( locale == null ) locale = Locale.getDefault();
        localeResolver.setLocale(request, response, locale);

        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    /**
     * Field 에러는 Spring BeanValidator를 이용하고,
     * Object 에러는 별도의 Validator로 구현
     * @param model
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/add")
    public String addItemV(Model model,
                            @Validated(C.class) @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        // 오브젝트 Validation 체크
        itemObjectValidator.validate(item, bindingResult);

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "validation/v3/addForm";
        }

        // 이하 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(Model model,
                       @PathVariable Long itemId,
                       @Validated(U.class) @ModelAttribute Item item,
                       BindingResult bindingResult) {

        // 오브젝트 Validation 체크
        itemObjectValidator.validate(item, bindingResult);

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "validation/v3/editForm";
        }


        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }


    public static class Builder
    {
        private final Model model;

        public Builder(Model model){
            this.model = model;
        }

        public Builder setRegions(@Nullable String key) {
            if (StringUtils.isEmpty(key)) key = "regions";

            Map<String, String> regions = new LinkedHashMap<>();
            regions.put("SEOUL", "서울");
            regions.put("BUSAN", "부산");
            regions.put("JEJU", "제주");
            model.addAttribute(key, regions);
            return this;
        }
        public Builder setItemTypes(@Nullable  String key) {
            if (StringUtils.isEmpty(key)) key = "itemTypes";
            model.addAttribute(key, ItemType.values());
            return this;
        }

        public Builder setDeliveryCodes(@Nullable  String key) {
            if (StringUtils.isEmpty(key)) key = "deliveryCodes";
            List<DeliveryCode> deliveryCodes = new ArrayList<>();
            deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
            deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
            deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
            model.addAttribute(key, deliveryCodes);
            return this;
        }

        public Builder set(String key, Object value) {
            model.addAttribute(key, value);
            return this;
        }

        public Model build(){
            return model;
        }
    }

}
