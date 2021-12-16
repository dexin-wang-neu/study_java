package cn.neu.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 2. 第二种处理异常的自定义方式   @ResponseStatus =>  底层调用  异常处理器ResponseStatusExceptionResolver
 * 处理  /dynamic_table  里user参数太多的异常
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "用户数量太多")
public class UserTooManyExceptionHandler extends RuntimeException{


    public UserTooManyExceptionHandler(String message){
        super(message);
    }
    public UserTooManyExceptionHandler(){

    }
}
