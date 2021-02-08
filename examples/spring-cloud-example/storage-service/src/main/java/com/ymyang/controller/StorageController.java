package com.ymyang.controller;

import com.alibaba.fastjson.JSON;
import com.ymyang.param.storage.StorageUpdateParam;
import com.ymyang.service.storage.StorageService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping
    public String updateStorage(@RequestBody StorageUpdateParam param) {
        log.info(" POST /api/storage, xid: " + RootContext.getXID() + ", param: " + JSON.toJSONString(param));

        storageService.updateStorage(param);

        return "ok";
    }

}
