package com.inho.mvc2.web.controller.validation;

import com.inho.mvc2.domain.repository.item.ItemRepository;
import com.inho.mvc2.domain.model.DeliveryCode;
import com.inho.mvc2.domain.model.Item;
import com.inho.mvc2.domain.model.ItemType;
import com.inho.mvc2.domain.validator.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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


@RequestMapping(value="/validation/v2/items")
public class ValidationV2Conroller
{
    private final ItemRepository itemRepository;
    private final LocaleResolver localeResolver;
    private final ItemValidator itemValidator;

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
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        Locale locale = localeResolver.resolveLocale(request);
        if ( locale == null ) locale = Locale.getDefault();
        localeResolver.setLocale(request, response, locale);

        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /**
     * BindingResult는 반드시 @ModelAttribute 다음에 넣는다(위치 중요)
     * BindingResult가 있으면, @ModelAttribute에 바인딩  오류가 발생해도 컨트롤러가 호출된다.
     * 만약, BindingResult가 없으면, 400 오류가 발생하고 오류페이지로 이동한다.
     * @param model
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    //@PostMapping("/add")
    public String addItem(Model model,
                          @ModelAttribute Item item,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        // 특정필드 검증 로직
        if ( StringUtils.isEmpty(item.getItemName()) ){
            bindingResult.addError( new FieldError("item", "itemName",  "상품 이름은 필수입니다."));
        }
        if ( item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000 ){
            bindingResult.addError( new FieldError("item", "price",  "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if ( item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError( new FieldError("item", "quantity",  "수량은 최대 9,999 까지 허용합니다."));
        }

        // 복합룰 검증
        if ( item.getPrice() != null && item.getQuantity() != null ){
            int resultPrice = item.getPrice() * item.getQuantity();
            if ( resultPrice < 10000 ){
                bindingResult.addError( new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "validation/v2/addForm";
        }

        // 이하 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    /**
     * FieldError 발생시, 기존 값 유지시키기
     * @param model
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    //@PostMapping("/add")
    public String addItemV2(Model model,
                          @ModelAttribute Item item,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        // 특정필드 검증 로직
        if ( StringUtils.isEmpty(item.getItemName()) ){
            bindingResult.addError( new FieldError("item", "itemName", item.getItemName(), false, null, null,  "상품 이름은 필수입니다."));
        }
        if ( item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000 ){
            bindingResult.addError( new FieldError("item", "price", item.getPrice(), false, null, null,  "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if ( item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError( new FieldError("item", "quantity", item.getQuantity(), false, null, null,  "수량은 최대 9,999 까지 허용합니다."));
        }

        // 복합룰 검증
        if ( item.getPrice() != null && item.getQuantity() != null ){
            int resultPrice = item.getPrice() * item.getQuantity();
            if ( resultPrice < 10000 ){
                bindingResult.addError( new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "validation/v2/addForm";
        }

        // 이하 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    /**
     * 에러 messageSource 사용
     * @param model
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    //@PostMapping("/add")
    public String addItemV3(Model model,
                            @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        // 특정필드 검증 로직
        if ( StringUtils.isEmpty(item.getItemName()) ){
            bindingResult.addError( new FieldError("item", "itemName", item.getItemName(), false,
                    new String[]{"requird.item.itemName"}, null,
                    "상품 이름은 필수입니다."));
        }
        if ( item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000 ){
            bindingResult.addError( new FieldError("item", "price", item.getPrice(), false,
                    new String[]{"range.item.price"}, new String[]{"1,000", "1,000,000"},
                    "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if ( item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError( new FieldError("item", "quantity", item.getQuantity(), false,
                    new String[]{"max.item.quantity"}, null,
                    "수량은 최대 9,999 까지 허용합니다."));
        }

        // 복합룰 검증
        if ( item.getPrice() != null && item.getQuantity() != null ){
            int resultPrice = item.getPrice() * item.getQuantity();
            if ( resultPrice < 10000 ){
                bindingResult.addError( new ObjectError("item",
                        new String[]{"totalPriceMin"}, null,
                        "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "validation/v2/addForm";
        }

        // 이하 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    /**
     * 에러코드 단순화 ( MessageCodeResolver 이해하기 )
     *  errCode를 어떻게 조합해서 만들어내는지?
     * @param model
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    //@PostMapping("/add")
    public String addItemV4(Model model,
                            @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        log.info("bindingResult.getObjectName={}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget={}", bindingResult.getTarget());

        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult,
                "itemName", "required");

        // 특정필드 검증 로직
        if ( StringUtils.isEmpty(item.getItemName()) ){
            // 규칙 = "requried" + ".' + objectName + "." + field
            // means = new String[] {"required.item.itemName", "required"}
            // 코드 수정없이, 메시지만 수정해서 좀더 상세한 메시지에 접근 가능하다.
            bindingResult.rejectValue("itemName", "required");

        }
        if ( item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000 ){
            bindingResult.rejectValue("price", "range", new Object[]{"1,000", "1,000,000"}, null);
        }
        if ( item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.rejectValue("quantity", "max");
        }

        // 복합룰 검증
        if ( item.getPrice() != null && item.getQuantity() != null ){
            int resultPrice = item.getPrice() * item.getQuantity();
            if ( resultPrice < 10000 ){
                bindingResult.reject("totalPriceMin", new Object[]{resultPrice}, null);
            }
        }

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "validation/v2/addForm";
        }

        // 이하 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    /**
     * 별도 Validator 분리하여 검증 수행
     * @param model
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    //@PostMapping("/add")
    public String addItemV5(Model model,
                            @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if ( itemValidator.supports(item.getClass()) ){
            itemValidator.validate(item, bindingResult);
        }

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "validation/v2/addForm";
        }

        // 이하 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * 컨트롤러 레벨에서 적용된다.
     * @param dataBinder
     */
    @InitBinder
    public void init(WebDataBinder dataBinder)
    {
        // 컨트롤러 요청 시 마다, WebDataBinder를 새로 생성한다.
        // WebDataBinder에 검증기를 추가하면 해당 컨트롤러에서는 검증기를 자동 적용할 수 있다.
        //@InitBinder -> 해당 컨트롤러에만 영향을 주며, 글로벌 설정은 별도로 해야한다.
        dataBinder.addValidators(itemValidator);
    }
    /*  Validator 전역 설정 설정방법 : 이렇게 사용하는 경우는 드믈다.
    *   public class AppMvcConfigure implements WebMvcConfigurer
    *   {
    *       @Override
    *       public Validator getValidator(){
    *           return new ItemValidator()
    *       }
    *   }
    * */

    /**
     * @Validated 애노테이션을 적용하면, validator를 자동 실행해준다.
     * @javax.validation.@Valid 를 사용하려면 의존관계 추가 필요 : 'org.springframework.boot:spring-boot-starter-validation'
     * @Validated는 스프링 전용 검증 애노테이션이고, @Valid는 자바 표준 검증 애노테이션이다. ( 둘 다 스프링이 지원 )
     * @param model
     * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/add")
    public String addItemV6(Model model,
                            @Validated @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "validation/v2/addForm";
        }

        // 이하 성공로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
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
