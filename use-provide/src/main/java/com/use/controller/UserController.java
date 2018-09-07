package com.use.controller;

import com.use.api.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author brusion
 * @date 2018/9/4
 */
@RestController
public class UserController implements UserService {


    @Override
    @RequestMapping("/index")
    public String index() {
        return "provide data: " + getClass().toString();
    }

}