package com.use.client;

import com.use.api.UserService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author brusion
 * @date 2018/9/4
 */
@FeignClient(name = "use-provide", fallback = UseClient.UseHystrix.class)
public interface UseClient extends UserService {

    @Component
    public class UseHystrix implements UseClient {
        @Override
        public String index() {
            return "Hystrix data : " + getClass().toString();
        }
    }
}
