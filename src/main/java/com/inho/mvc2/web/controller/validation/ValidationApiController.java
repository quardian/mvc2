package com.inho.mvc2.web.controller.validation;

import com.inho.mvc2.web.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationApiController {

    /**
     * API의 경우 3 경우 발생
     *  1) 성공
     *  2) 실패 : JSON을 객체로 생성하는 것 자체를 실패 ( 400 에러 )
     *  3) 검증 오류 요청 : JSON 객체 생성 성공 후, 검증 실패
     *
     *  HttpMessageConverter는 @ModelAttribute와 다르게 각각의 필드 단위로 적용되는 것이 아니라, 전체 객체 단위로 적용된다.
     *  따라서 메시지 컨버터의 작동이 성공해서 Item 객체를 만들어야 @Valid, @Validated가 적용된다.
     *
     * @ModelAttribute는 필드 단위로 정교하게 바인딩이 적용된다. 특정 필드가 바인딩 되지 않아도 나머지 필드는 정상 바인딩 되고,
     * Validator를 사용한 검증도 적용할 수 있다.
     *
     * @RequestBody는 HttpMessageConverter 단계에서 JSON 덱이터를 객체로 변경하지 못하면 이후 단계 자체가 진행않고 예외 발생
     * 컨트롤러도 호출되지 않고, Validator도 적용할 수 없다.
     *
     * HttpMessageConveter 단계에서 실패시, 원하는 메시지 형태로 바꾸려면 별도 예외처리가 필요하다.
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/add")
    public Object add(@RequestBody @Validated ItemSaveForm form,
                       BindingResult bindingResult)
    {
        log.info("API 컨트롤러 호출");
        if ( bindingResult.hasErrors() )
        {
            log.info("검증 오류 : {}", bindingResult);
            return bindingResult.getAllErrors();
        }
        return form;
    }
}
