package com.ymyang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymyang.entity.AccountEntity;

public interface AccountService extends IService<AccountEntity> {

    void updateMoney(AccountEntity param);

}
