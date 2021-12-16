package cn.neu.admin.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 处理整个web controller的异常
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 第一种自定义的异常处理方式 @ControllerAdvice+@ExceptionHandler =>  底层调用 异常处理器: ExceptionHandlerExceptionResolver
     * @param e
     * @return
     */
    @ExceptionHandler({ArithmeticException.class,NullPointerException.class})  //处理异常 数学运算异常，空指针异常
    public String handleArithException(Exception e){
       log.error("异常是：{}",e);
        return "login";  //视图地址
    }
}
