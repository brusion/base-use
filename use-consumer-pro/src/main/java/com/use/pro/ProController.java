package com.use.pro;

import com.use.client.UseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author brusion
 * @date 2018/9/4
 */
@RestController
public class ProController {

    @Autowired
    private UseClient useClient;

    @RequestMapping("/index")
    public String index() {
        return useClient.index();
    }
}
