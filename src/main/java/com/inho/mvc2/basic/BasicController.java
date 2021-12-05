package com.inho.mvc2.basic;

import com.inho.mvc2.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/basic")
public class BasicController {

    /**
     * [01] 기본 텍스트 출력
     * th:text  , th:utext
     * [[${}]]  , [(${})]
     * @param model
     * @return
     */
    @GetMapping(value="/text-basic")
    public String textBasic(Model model)
    {
        model.addAttribute("data", "Hello String1 <em>!</em>");

        return "basic/text-basic";
    }

    /**
     * [02] Spring EL 표현식
     * @param model
     * @return
     */
    @GetMapping(value="/variable")
    public String variable(Model model)
    {
        User userA = new User("userA", 10);
        User userB = new User("userB", 20);
        /*  Object Spring EL
            userA.username;
            userA['username']
            userA.getUserName()
        * */

        // LIST
        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        /*
            List Spring EL  : list.get(0) ==> list[0]
            list[0].username;
            list[0]['username']
            list[0].getUserName()
         */

        // MAP
        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        /*
            List Spring EL : map.get("userA") ==> map['userA']
            map['userA'].username;
            map['userA']['username']
            map['userA'].getUserName()
         */


        model.addAttribute("userA", userA);
        model.addAttribute("userList", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
    }



    /**
     * http://localhost:8080/basic/basic-objects?paramData=helloParam
     * [03] 타임리프 기본 객체 제공
     * ${ #ctx }            : 컨텍스트 오브젝트
     * #{ #vars }           : 컨텍스트 변수들
     * ${ #request }        : (Web Context 전용) HttpServletRequest 오브젝트
     * ${ #response }       : (Web Context 전용) HttpServletResponse 오브젝트
     * ${ #session }        : (Web Context 전용) HttpSession 오브젝트
     * ${ #serveltContext } : (Web Context 전용) ServletContext 오브젝트
     * ${ #locale }         : 컨텍스트 locale
     *
     * #request.getParameter("data") 대신 ..
     * HTTP 요청 파라미터 접근               : ${ param.paramData }
     * HTTP 세션 접근                       : ${ session.sessionData }
     * application/servlet context 속성    : ${ application.foo }
     * 스프링빈 접근                        : ${ @HelloBean.hello('Srping!') }
     *
     *
     * @param session
     * @return
     */
    @GetMapping(value="/basic-objects")
    public String basicObject(HttpSession session)
    {
        session.setAttribute("sessionData", "Hello Session");

        return "basic/basic-objects";
    }


    /**
     * 타임리프 유틸리티 객체들 : 문자, 숫자, 날짜, URI등 편리하게 다루는 다양한 유틸리티 객체 제공
     * #execInfo    : 처리되는 템플릿에 대한 정보
     * #messsage    : 메시지, 국제화 처리
     * #uris        : URI 이스케이프 지원
     * #dates       : java.util.Date 서식 지원
     * #calendars   : java.util.Canendar 서식 지원
     * #temporals   : java8 날짜 서식 지원
     * #numbers     : 숫자 서식 지원
     * #strings     : 문자관련 편의 기능
     * #objects     : 객체 관련 기능 제공
     * #bools       : boolean 관련 기능 제공
     * #arrays      : 배열 관련 기능 제공
     * #lists, #sets, #maps : 컬렉션 관련 기능 제공
     * #ids         : 아이디 처리 관련 기능 제공
     *
     * @return
     */
    @GetMapping(value="/utils")
    public String utils(Model model, HttpSession session)
    {
        model.addAttribute("localDatetime", LocalDateTime.now());
        model.addAttribute("date", new Date());

        model.addAttribute("intVal1", 10);
        model.addAttribute("intVal2", 1000000);

        model.addAttribute("decimal1", 10.34);
        model.addAttribute("decimal2", 1000000.34567);

        model.addAttribute("bigDecimalVal", BigDecimal.valueOf(1000000.34567) );

        model.addAttribute("percent", 0.76576);

        model.addAttribute("nullVal", null);
        model.addAttribute("emptyVal", "");
        model.addAttribute("stringVal", " string ");

        model.addAttribute("arr", new String[] {"A","B", "C"});
        model.addAttribute("list", Arrays.asList("A", "B", "C"));

        String javaScript = "alert('ttl');\n";
        model.addAttribute("javaScript", javaScript);

        session.setAttribute("sessionData", "Hello Session");

        return "basic/utils";
    }

    @GetMapping(value="/link")
    public String link(Model model)
    {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");

        return "basic/link";
    }


    @GetMapping(value="/literal")
    public String literal(Model model)
    {
        model.addAttribute("data", "world!!");

        return "basic/literal";
    }


    @GetMapping(value="/operation")
    public String operation(Model model)
    {
        model.addAttribute("nullData", null);
        model.addAttribute("emptyData", "");
        model.addAttribute("data", "world!!");

        return "basic/operation";
    }



    @GetMapping(value="/attribute")
    public String attribute(Model model)
    {
        User user = new User("이인호", 18);
        model.addAttribute("user", user);

        return "basic/attribute";
    }


    @GetMapping(value="/each")
    public String each(Model model)
    {
        List<User> list = getUsers();

        model.addAttribute("users", list);

        return "basic/each";
    }


    /**
     * if, unless
     * @param model
     * @return
     */
    @GetMapping(value="/condition")
    public String condition(Model model)
    {
        List<User> list = getUsers();

        model.addAttribute("users", list);

        return "basic/condition";
    }

    @GetMapping(value="/comments")
    public String comments(Model model)
    {
        model.addAttribute("data", "Spring!");

        return "basic/comments";
    }



    private List<User> getUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User("userA", 10));
        list.add(new User("userB", 20));
        list.add(new User("userC", 30));
        return list;
    }
}
