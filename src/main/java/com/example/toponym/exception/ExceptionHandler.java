package com.example.toponym.exception;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 杨宇帆
 */
@Slf4j
@Component
public class ExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attr = new HashMap<>();
        if (ex instanceof ServiceException) {
            response.setStatus(HttpServletResponse.SC_OK);
            attr.put("status", ((ServiceException)ex).getData().getStatus());
            attr.put("data", ((ServiceException)ex).getData().getData());
            attr.put("msg", ((ServiceException)ex).getData().getMsg());
            log.error("ServiceAccessException:msg:[{}]:[{}]", ((ServiceException) ex).getData().getMsg(), ex);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            attr.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            attr.put("msg", ex.getMessage() == null ? ex : ex.getMessage());
            attr.put("data", null);
            log.error("Exception", ex);
        }
        view.setAttributesMap(attr);
        modelAndView.setView(view);
        return modelAndView;
    }
}
