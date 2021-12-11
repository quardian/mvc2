package com.inho.mvc2.web.common.controlleradvice;

import com.inho.mvc2.web.common.exceptions.UserException;
import com.inho.mvc2.web.model.ErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ControllerAdvice
 * @ControllerAdvice는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여해주는 역할을 한다.
 * @ControllerAdvice에 대상을 지정안하면 모든 컨트롤러에 적용된다(글로벌 적용)
 *
 * @RestControllerAdvice는 @ControllerAdvice와 같고, @ResponseBody가 추가되어 있다.
 *
 * 대상 컨트롤러 지정 방법
 * 1) 특정 애노테이션
 *      @ControllerAdvice (annotations = RestController.class)
 * 2) 특정 패키지내 모든 컨트롤러
 *      @ControllerAdvice("com.inho.mvc2.api")
 * 3) 특정 지정된 컨트롤러 클래서
 *      @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbsctactController.class})
 * 4) 미지정 : 전체 컨트롤러
 */

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
public class RestConrollerAdvice {
    /*
        @ExceptionHandler
        스프링은 API 예외 처리 문제를 해결하기 위해 @ExceptionHandler 라는 애노테이션을 사용하는 예외처리 기능을 제공하는데,
        이것은 ExceptionHandlerExceptionResolver 이다. (스프링 기본 제공 )
        ExceptionResolver 중에 우선 순위가 가장 높다. API 예외 처리는 대부분 이 기능을 사용한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalArgumentExceptionHandler(IllegalArgumentException err)
    {
        log.info("illegalArgumentExceptionHandler {}", err);
        ErrorResult result = new ErrorResult("error.bad.request", err.getMessage());
        return result;
    }

    /**
     * @ExceptionHandler만 지정하고, 입력 파라메타에 UserException만 지정해도 해당 에러를 처리한다.
     * @param err
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException err)
    {
        log.info("userExHandler {}", err);
        ErrorResult result = new ErrorResult("error.bad.request", err.getMessage());

        return new ResponseEntity( result, HttpStatus.BAD_REQUEST);
    }


    /**
     * 공통 예외처리
     * @param err
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> exHandler(Exception err)
    {
        log.info("exHandler {}", err);
        ErrorResult result = new ErrorResult("error.internal", err.getMessage());

        return new ResponseEntity( result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
