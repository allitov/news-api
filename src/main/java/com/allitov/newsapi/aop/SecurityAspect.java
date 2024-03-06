package com.allitov.newsapi.aop;

import com.allitov.newsapi.exception.IllegalDataAccessException;
import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

//@Aspect
//@Component
//@RequiredArgsConstructor
//public class SecurityAspect {
//
//    private final UserService userService;
//
//    @Pointcut(value = "execution(* com.allitov.newsapi.web.controller.v2.UserController.findById(..))")
//    public void controllerFindUserByIdPointcut() {}
//
//    @Before(value = "controllerFindUserByIdPointcut() && args(userId, userDetails)", argNames = "userDetails, userId")
//    public void controllerFindUserByIdAdvice(UserDetailsImpl userDetails, Long userId) {
//        if (userDetails.getAuthorities().size() > 1) {
//            return;
//        }
//
//        User user = userService.findUserByName(userDetails.getUsername());
//        if (!user.getId().equals(userId)) {
//            throw new IllegalDataAccessException(MessageFormat.format(
//                    "User with id = {0} cannot get data of user with id = {1}", user.getId(), userId));
//        }
//    }
//}

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
            throw new IllegalDataAccessException(MessageFormat.format(
                    "User with id = {0} cannot get data of user with id = {1}", userDetails.getId(), userId));
        }
    }
}
