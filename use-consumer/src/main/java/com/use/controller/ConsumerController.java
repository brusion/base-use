package com.use.controller;

import com.use.api.UserService;
import com.use.client.UseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author brusion
 * @date 2018/9/4
 */
@RestController
public class ConsumerController implements UserService {

    @Autowired
    UseClient useClient;

    @Override
    @RequestMapping("/index")
    public String index() {
        return useClient.index();
    }
}
