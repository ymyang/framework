package com.ymyang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymyang.entity.StorageEntity;

public interface StorageService extends IService<StorageEntity> {

    void updateStorage(StorageEntity param);

}
