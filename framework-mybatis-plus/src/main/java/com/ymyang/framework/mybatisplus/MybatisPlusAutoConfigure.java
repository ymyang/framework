package com.ymyang.framework.mybatisplus;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class MybatisPlusAutoConfigure {
}
