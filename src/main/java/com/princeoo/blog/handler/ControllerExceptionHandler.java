package com.princeoo.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHander(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Request URL : {},Exception: {}",request.getRequestURL(),e);

        //@ControllerAdvice把所有的异常都拦截了，所以需要判断，如果是ResponseStatus（标识了状态码）的异常则返回
        //e.getClass(), ResponseStatus.class（异常的类，状态的类型）
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!= null){
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");

        return mv;
    }
}
