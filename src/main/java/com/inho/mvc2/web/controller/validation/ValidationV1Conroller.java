package com.inho.mvc2.web.controller.validation;

import com.inho.mvc2.domain.repository.item.ItemRepository;
import com.inho.mvc2.domain.model.DeliveryCode;
import com.inho.mvc2.domain.model.Item;
import com.inho.mvc2.domain.model.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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


@RequestMapping(value="/validation/v1/items")
public class ValidationV1Conroller
{
    private final ItemRepository itemRepository;
    private final LocaleResolver localeResolver;

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
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpServletRequest request, HttpServletResponse response) {

        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    /**
     * 직접 오류처리 시 문제점
     * 1) 뷰템플릿에 중복처리가 많고, 비슷한 패턴이 많다.
     * 2) 타입 오류 처리가 안됨 (int값에 문자열 들어오는 경우)
     *    ==> 오류로 인해 데이터가 살아져서 고객에게 다시 보여줄 수가 없음
     * @param model
     * @param item
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/add")
    public String addItem(Model model, @ModelAttribute Item item,
                          RedirectAttributes redirectAttributes) {

        // 검증 오류 결과 보관
        Map<String, String> errors = new HashMap<>();

        // 특정필드 검증 로직
        if ( StringUtils.isEmpty(item.getItemName()) ){
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if ( item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000 ){
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }
        if ( item.getQuantity() == null || item.getQuantity() >= 9999){
            log.info("errors={}", errors);
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }

        // 복합룰 검증
        if ( item.getPrice() != null && item.getQuantity() != null ){
            int resultPrice = item.getPrice() * item.getQuantity();
            if ( resultPrice < 10000 ){
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        // 검증 실패시 다시 입력 폼으로
        boolean hasError = !errors.isEmpty();
        if ( hasError ){
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }

        // 이하 성공로직
        
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/{itemId}";
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
