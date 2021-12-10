package com.inho.mvc2.web.controller.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/template")
public class TemplateController {

    /**
     * 다른 템플릿의 코드 조각을 갖어다 쓴다.
     * @return
     */
    @GetMapping("/fragment/fragmentMain")
    public String fragmentMain()
    {
        return "template/fragment/fragmentMain";
    }


    /**
     * 나의 코드조각 일부로 다른 곳으로 넘겨서 작성
     * @return
     */
    @GetMapping("/layout/layoutMain")
    public String layoutMain()
    {
        return "template/layout/layoutMain";
    }



    /**
     *
     * @return
     */
    @GetMapping("/layoutExtend/layoutExtendMain")
    public String layoutExtendMain()
    {
        return "template/layoutExtend/layoutExtendMain";
    }


    /**
     * <html
     *      layout:decorate="~{layout}">
     *      : 컨텐츠 템플릿을 장식할 레이아웃 템플릿 지정
     *      컨텐츠 템플릿의 head 내 엘리먼트들은 병합된다.
     * <title
     *      layout:title-pattern="$LAYOUT_TITLE - $CONTENT_TITLE">My content page
     * </title>
     *      : $LAYOUT_TITLE : 장식 템플릿에서 찾은 title 값을 표현
     *      : $CONTENT_TITLE: 컨턴츠 템플릿에서 발견된 title 값을 표현
     *      ==> 각각의 토큰들은 결과 페이지에 타이틀로 대체된다.
     *
     * <div
     *      layout:insert="~{modal :: modal(title='Greetings') }">
     *      <p layout:fragment="model-content">Hi there!</p>
     * </div>
     *      : 타임리프의 th:insert와 비슷하지만, 목적 템플릿에 전체 template fragments를 전달할 수 있다.
     *
     * <div
     *      layout:replace="~{modal :: modal(title='Greetings') }">
     *      <p layout:fragment="model-content">Hi there!</p>
     * </div>
     *      : 타임리프의 th:insert와 비슷하지만, 목적 템플릿에 전체 template fragments를 전달할 수 있다.
     *
     * <div
     *      layout:frgment="fragment-name">
     *      : 모든 것을 하나로 묶는 접착제 역할.
     *      동일한 이름을 공유하는 섹션으로 대체할 수 있는 재사용 가능한 템플릿 또는 레이아웃 섹션을 표시
     *      템플릿 내 이름은 공유해야한다.
     *
     * @return
     */
    // thymeleaf layout dialect
    // implementation group: 'nz.net.ultraq.thymeleaf', name: 'thymeleaf-layout-dialect', version: '2.5.3'
    @GetMapping("/layoutDialect/layoutDialectMain")
    public String layoutDialectMain()
    {
        return "template/layoutDialect/layoutDialectMain";
    }
}
