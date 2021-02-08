package com.ymyang.generator.service;

import com.ymyang.generator.config.GeneratorConfig;
import com.ymyang.generator.config.Modules;
import com.ymyang.generator.dao.SysGeneratorDao;
import com.ymyang.generator.utils.GenerationException;
import com.ymyang.generator.utils.GenerationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 */
@Service
public class CustomGeneratorService {

    //    private String tmpTableName = "CustomGeneratorService_" + RandomStringUtils.randomNumeric(5);
    @Autowired
    private SysGeneratorDao sysGeneratorDao;

    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        return sysGeneratorDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return sysGeneratorDao.queryTotal(map);
    }

    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorDao.queryTable(tableName);
    }

    public List<Map<String, Object>> queryColumns(String tableName) {
        return sysGeneratorDao.queryColumns(tableName);
    }

    public void generatorCode(GeneratorConfig generatorConfig) {

        List<Modules> modules = generatorConfig.getModules();

        if (modules == null || modules.size() == 0) {
            return;
        }

        for (Modules module : modules) {
            if (StringUtils.isEmpty(module.getName())) {
                throw new RuntimeException("generator.moduleName.name 不能为空！");
            }
            if (module.getTables() == null || module.getTables().isEmpty()) {
                throw new RuntimeException("generator.moduleName.tables 不能为空！");
            }
        }

        for (Modules module : modules) {
            for (String table : module.getTables()) {
                generatorCode(
                        generatorConfig,
                        module.getName(),
                        table
                );
            }
        }


    }

    public void generatorCode(GeneratorConfig generatorConfig, String moduleName, String tableName) {

        if (tableName == null) {
            throw new GenerationException("生成错误：配置表名不能为空！");
        }


        //查询表信息
        Map<String, String> table = queryTable(tableName);

        //查询列信息
        List<Map<String, Object>> columns = queryColumns(tableName);

        // 重置表名
        table.put("tableName", tableName);

        //生成代码
        GenerationUtils.generatorCode(table, columns, generatorConfig, moduleName);

    }

}
