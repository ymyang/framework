package com.ymyang.controller;

import com.alibaba.fastjson.JSON;
import com.ymyang.entity.AccountEntity;
import com.ymyang.service.AccountService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public String updateMoney(@RequestBody AccountEntity param) {
        log.info(" POST /api/account, xid: " + RootContext.getXID() + ", param: " + JSON.toJSONString(param));

        accountService.updateMoney(param);

        return "ok";
    }

}
