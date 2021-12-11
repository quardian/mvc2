package com.inho.mvc2.web.controller.item;

import com.inho.mvc2.domain.model.DeliveryCode;
import com.inho.mvc2.domain.model.Item;
import com.inho.mvc2.domain.model.ItemType;
import com.inho.mvc2.domain.repository.item.ItemRepository;
import com.inho.mvc2.domain.validator.ItemSaveObjectValidator;
import com.inho.mvc2.domain.validator.ItemUpdateObjectValidator;
import com.inho.mvc2.web.common.annotation.LoginRequired;
import com.inho.mvc2.web.controller.validation.ValidationV4Conroller;
import com.inho.mvc2.web.model.form.item.ItemSaveForm;
import com.inho.mvc2.web.model.form.item.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@LoginRequired
public class ItemController {
    private final ItemRepository itemRepository;
    private final LocaleResolver localeResolver;
    private final ItemSaveObjectValidator itemSaveObjectValidator;
    private final ItemUpdateObjectValidator itemUpdateObjectValidator;

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



    @GetMapping("")
    public String items(Model model) {

        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "items/items";
    }

    @LoginRequired(value = false)
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "items/item";
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpServletRequest request, HttpServletResponse response) {
        Locale locale = localeResolver.resolveLocale(request);
        if ( locale == null ) locale = Locale.getDefault();
        localeResolver.setLocale(request, response, locale);

        model.addAttribute("item", new ItemSaveForm());
        return "items/addForm";
    }

    /**
     * Field 에러는 Spring BeanValidator를 이용하고,
     * Object 에러는 별도의 Validator로 구현
     * @param model
     * @param form
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/add")
    public String addItemV(Model model,
                           @Validated @ModelAttribute("item") ItemSaveForm form,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        // 오브젝트 Validation 체크
        itemSaveObjectValidator.validate(form, bindingResult);

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "items/addForm";
        }

        // 이하 성공로직
        Item item = form.asItem();
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", new ItemUpdateForm(item) );

        return "items/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(Model model,
                       @PathVariable Long itemId,
                       @Validated @ModelAttribute("item") ItemUpdateForm form,
                       BindingResult bindingResult) {

        // 오브젝트 Validation 체크
        itemUpdateObjectValidator.validate(form, bindingResult);

        // 검증 실패시 다시 입력 폼으로
        if ( bindingResult.hasErrors() ){
            model.addAttribute("bindingResult", bindingResult);
            return "items/editForm";
        }

        Item item = form.asItem();
        itemRepository.update(itemId, item);
        return "redirect:/items/{itemId}";
    }


}
