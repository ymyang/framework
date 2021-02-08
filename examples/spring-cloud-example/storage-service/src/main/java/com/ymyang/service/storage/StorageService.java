package com.ymyang.service.storage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymyang.entity.storage.StorageEntity;
import com.ymyang.param.storage.StorageUpdateParam;

public interface StorageService extends IService<StorageEntity> {

    void updateStorage(StorageUpdateParam param);

}
