package com.ymyang.feign;

import com.ymyang.param.account.AccountUpdateParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service")//, fallback = TestFallback.class)
public interface AccountFeignClient {

    @PostMapping("/api/account")
    String updateMoney(@RequestBody AccountUpdateParam body);
}
