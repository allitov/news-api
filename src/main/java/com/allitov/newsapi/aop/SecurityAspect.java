package com.allitov.newsapi.aop;

import com.allitov.newsapi.exception.ExceptionMessage;
import com.allitov.newsapi.exception.IllegalDataAccessException;
import com.allitov.newsapi.security.UserDetailsImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {

    @Before(
            value = "execution(* com.allitov.newsapi.web.controller.v2.UserController.findById(..)) " +
                    "&& args(userId, userDetails)",
            argNames = "userId, userDetails"
    )
    public void controllerFindUserByIdAdvice(Long userId, UserDetailsImpl userDetails) {
        if (userDetails.getAuthorities().size() > 1) {
            return;
        }

        if (!userDetails.getId().equals(userId)) {
            throw new IllegalDataAccessException(String.format(
                    ExceptionMessage.USER_DATA_ILLEGAL_ACCESS, userDetails.getId(), userId));
        }
    }
}
