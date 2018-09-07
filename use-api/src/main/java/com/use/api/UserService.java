package com.use.api;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author brusion
 * @date 2018/9/4
 */
public interface UserService {

    @RequestMapping("/index")
    String index();
}
