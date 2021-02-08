package com.ymyang.framework.mybatisplus.utils;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MybatisUtil {

    /**
     * @param sqlSession
     * @param mapperId
     * @param resultType
     * @return
     */
    public static void createCommonMappedStatementIfNeed(SqlSession sqlSession, String mapperId, Class<?> resultType) {

//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.getCustomSqlSegment()
        String joinSql = "SELECT ${columns} FROM ${table} ${ew.customSqlSegment}";
        newSelectMappedStatement(joinSql, sqlSession, mapperId, resultType);
    }


    /**
     * 功能描述:
     * 对符合 mybatis.dtd形式的sql进行动态sql解析并执行，返回Map结构的数据集
     *
     * @param: executeSql 要执行的sql ， sqlSession 数据库会话，namespace 命名空间，mapper_id mapper的ID，bounds 分页参数
     * @return:
     * @auther:
     * @date: 2018/11/9 16:17
     */
    public static MappedStatement newSelectMappedStatement(String executeSql,
                                                           SqlSession sqlSession,
                                                           String mapperId,
                                                           Class<?> resultType
    ) {
        Configuration configuration = sqlSession.getConfiguration();
        try {
            MappedStatement msTest = configuration.getMappedStatement(mapperId);
            if (msTest != null) {
                return msTest;
            }
        } catch (Exception e) {

        }

        synchronized (configuration) {
            try {
                MappedStatement msTest = configuration.getMappedStatement(mapperId);
                if (msTest != null) {
                    return msTest;
                }
            } catch (Exception e) {

            }
            // 1. 对executeSql 加上script标签
            StringBuffer sb = new StringBuffer();
            sb.append("<script>");
            sb.append(executeSql);
            sb.append("</script>");

            LanguageDriver languageDriver = configuration.getDefaultScriptingLanguageInstance();  // 2. languageDriver 是帮助我们实现dynamicSQL的关键
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sb.toString(), resultType);  //  泛型化入参
            return newSelectMappedStatement(configuration, mapperId, sqlSource, resultType);
        }
    }

    //
    private static MappedStatement newSelectMappedStatement(Configuration configuration,
                                                            String mapperId,
                                                            SqlSource sqlSource,
                                                            final Class<?> resultType) {

        // 构建一个 select 类型的ms ，通过制定SqlCommandType.SELECT
        MappedStatement mappedStatement = new MappedStatement.Builder(   // 注意！！-》 这里可以指定你想要的任何配置，比如cache,CALLABLE,
                configuration, mapperId, sqlSource, SqlCommandType.SELECT)   // -》 注意，这里是SELECT,其他的UPDATE\INSERT 还需要指定成别的
                .resultMaps(new ArrayList<ResultMap>() {
                    {
                        add(new ResultMap.Builder(configuration,
                                "defaultResultMap",
                                resultType,
                                new ArrayList<ResultMapping>(0)).build());
                    }
                })
                .build();
        configuration.addMappedStatement(mappedStatement); // 加入到此中去
        return mappedStatement;
    }

    /**
     * @param tableInfo
     * @param prefix
     * @return
     */
    public static String getAllSqlSelectWithPrefix(TableInfo[] tableInfo, String[] prefix, int tableStartIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tableInfo.length; i++) {
            int finalI = i;
            String collect = Stream.of(tableInfo[i].getAllSqlSelect().split(StringPool.COMMA))
                    .map(item -> "t" + (tableStartIndex + finalI) + StringPool.DOT + item + (StringUtils.isEmpty(prefix[finalI]) ? "" : " AS `" + prefix[finalI] + item + "`"))
                    .collect(Collectors.joining(StringPool.COMMA));
            stringBuilder.append(collect).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

}