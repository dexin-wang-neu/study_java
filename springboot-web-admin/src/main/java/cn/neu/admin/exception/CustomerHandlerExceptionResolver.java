package cn.neu.admin.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义异常解析器
 * 可以作为全局默认的异常处理器
 */
@Component //把自定义的异常解析器交给容器
@Order(value = Ordered.HIGHEST_PRECEDENCE)  //设置最高优先级，数字越小，优先级越高，当发生异常时，在选择异常处理器解析器时先用我们的，
                                            // 再用DefaultHandlerExceptionResolver,HandlerExceptionResolverComposite
public class CustomerHandlerExceptionResolver implements HandlerExceptionResolver {

    /**
     * 自定义异常处理器
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return  直接返回modelAndView页面，或者向tomcat发送一个error请求，之后这个error请求被默认的异常处理器controller处理
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            response.sendError(511,"我喜欢的错误");//error请求被默认的异常处理器basiErrorController处理
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ModelAndView(); //这里返回了modelAndView,所以轮不到其他异常解析器解析了。看源码
    }
}
