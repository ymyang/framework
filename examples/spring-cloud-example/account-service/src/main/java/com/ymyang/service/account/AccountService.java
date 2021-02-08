package com.ymyang.service.account;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymyang.entity.account.AccountEntity;
import com.ymyang.param.account.AccountUpdateParam;

public interface AccountService extends IService<AccountEntity> {

    void updateMoney(AccountUpdateParam param);

}
