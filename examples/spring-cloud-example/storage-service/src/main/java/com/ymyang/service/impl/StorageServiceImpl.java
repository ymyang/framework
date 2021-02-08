package com.ymyang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ymyang.entity.StorageEntity;
import com.ymyang.mapper.StorageMapper;
import com.ymyang.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, StorageEntity> implements StorageService {

    @Override
    public void updateStorage(StorageEntity param) {
        QueryWrapper<StorageEntity> wrapper = Wrappers.query();
        wrapper.eq("commodity_code", param.getCommodityCode());
        StorageEntity storage = this.getOne(wrapper);
        if (storage == null) {
            throw new RuntimeException("Storage not found");
        }
        if (storage.getCount() < param.getCount()) {
            throw new RuntimeException("Storage is not enough");
        }

        storage.setCount(storage.getCount() - param.getCount());

        this.updateById(storage);
    }

}
