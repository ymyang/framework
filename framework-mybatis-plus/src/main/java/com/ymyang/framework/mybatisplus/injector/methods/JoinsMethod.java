package com.ymyang.framework.mybatisplus.injector.methods;

import com.ymyang.framework.mybatisplus.annotation.InnerJoin;
import com.ymyang.framework.mybatisplus.annotation.LeftJoin;
import com.ymyang.framework.mybatisplus.utils.MybatisUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class JoinsMethod extends AbstractMethod {

    Logger log = LoggerFactory.getLogger(JoinsMethod.class);

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        return null;
    }


    /**
     * @param builderAssistant
     * @param mapperClass
     * @param modelClass
     * @param resultType
     * @param joins            只能是LeftJoin, InnerJoin
     * @param mapperMethodName
     * @return
     */
    public MappedStatement injectJoinsMappedStatement(
            MapperBuilderAssistant builderAssistant,
            Class<?> mapperClass,
            Class<?> modelClass,
            Class<?> resultType,
            List<Annotation> joins,
            String mapperMethodName
    ) {

        TableInfo tableInfo = initTableInfo(builderAssistant.getConfiguration(), mapperClass, modelClass);
        if (!tableInfo.isAutoInitResultMap()) {
            throw new RuntimeException(modelClass.getName() + "，@TableName必须开启autoResultMap");
        }

        inject(builderAssistant, mapperClass, modelClass, tableInfo);

        TableInfo[] tableInfoJoins = new TableInfo[joins.size()];
        for (int i = 0; i < joins.size(); i++) {
            if (!LeftJoin.class.isInstance(joins.get(i)) && !InnerJoin.class.isInstance(joins.get(i))) {
                throw new RuntimeException(mapperClass.getName() + "." + modelClass.getName() + "中的注解只能是@LeftJoin, @InnerJoin");
            }
            Class joinAttribute = getAnnotationAttributeValue(joins.get(i), "join", Class.class);
            Class mapperAttribute = getAnnotationAttributeValue(joins.get(i), "mapper", Class.class);
            tableInfoJoins[i] = initTableInfo(builderAssistant.getConfiguration(), mapperAttribute, joinAttribute);
            if (!tableInfoJoins[i].isAutoInitResultMap()) {
                throw new RuntimeException(joinAttribute.getName() + "，@TableName必须开启autoResultMap");
            }
        }

        // 表join信息、字段信息
        StringBuilder tableJoinBuilder = new StringBuilder();
        StringBuilder selectJoinBuilder = new StringBuilder();
        String[] columnPrefix = new String[joins.size()];
        tableJoinBuilder.append(tableInfo.getTableName()).append(" t1 ");
        for (int i = 0; i < joins.size(); i++) {
            if (joins.get(i) instanceof LeftJoin) {
                tableJoinBuilder.append(" LEFT JOIN ");
                LeftJoin join = (LeftJoin) joins.get(i);
                tableJoinBuilder.append(tableInfoJoins[i].getTableName()).append(" t").append(i + 2)
                        .append(" ON ").append(join.on());
                if (!StringUtils.isEmpty(join.select())) {
                    selectJoinBuilder.append(join.select()).append(",");
                }
                if (StringUtils.isEmpty(join.property())) {
                    columnPrefix[i] = "";
                } else {
                    columnPrefix[i] = join.property() + DOT;
                }
            }
            if (joins.get(i) instanceof InnerJoin) {
                tableJoinBuilder.append(" INNER JOIN ");
                InnerJoin join = (InnerJoin) joins.get(i);
                tableJoinBuilder.append(tableInfoJoins[i].getTableName()).append(" t").append(i + 2);
                if (!StringUtils.isEmpty(join.on())) {
                    tableJoinBuilder.append(" ON ").append(join.on());
                }
                if (!StringUtils.isEmpty(join.select())) {
                    selectJoinBuilder.append(join.select()).append(",");
                }
                if (StringUtils.isEmpty(join.property())) {
                    columnPrefix[i] = "";
                } else {
                    columnPrefix[i] = join.property() + DOT;
                }
            }
        }
        String sqlSelect;
        if (selectJoinBuilder.length() > 0) {
            sqlSelect = selectJoinBuilder.substring(0, selectJoinBuilder.length() - 1);
        } else {
            sqlSelect = MybatisUtil.getAllSqlSelectWithPrefix(new TableInfo[]{tableInfo}, new String[]{""}, 1) + ","
                    + MybatisUtil.getAllSqlSelectWithPrefix(tableInfoJoins, columnPrefix, 2);
        }

        SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
        String selectColumns = SqlScriptUtils.convertChoose(
                String.format("%s != null and %s != null", WRAPPER, Q_WRAPPER_SQL_SELECT),
                SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT),
                sqlSelect);


        String sql = String.format(sqlMethod.getSql(), selectColumns, tableJoinBuilder,
                sqlWhereEntityWrapper(true, unsetLoginDelete(tableInfo)), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        ResultMap newResultMap = createResultMap(mapperClass, joins, resultType, null, "");

        return addMappedStatement(mapperClass, mapperMethodName, sqlSource, SqlCommandType.SELECT, null,
                newResultMap.getId(), resultType, new NoKeyGenerator(), null, null);

    }


    /**
     * 递归方式生成resultMap
     *
     * @param mapperClass
     * @param joins
     * @param resultType
     * @param currentJoinAnnotation
     * @param parentProperty
     * @return
     */
    ResultMap createResultMap(Class<?> mapperClass, List<Annotation> joins, Class<?> resultType,
                              Annotation currentJoinAnnotation, String parentProperty) {

        ResultMap resultMap = getOrNewResultMapFromClass(resultType, mapperClass, null);

//      {"user" : User, "user.role" : Role, "user.role.permission" : Permission, "user.group" : group, "user.group.permission" : gp }
//      {"me": User, "me.me": User, "me.role": Role, "me.me.me": User, "me.me.me.me": User}

        HashMap<String, ResultMap> nestedResultMaps = new HashMap<>(joins.size());

        for (Annotation leftJoin : joins) {
            String property = getAnnotationAttributeValue(leftJoin, "property", String.class);
            Class mapper = getAnnotationAttributeValue(leftJoin, "mapper", Class.class);
            if (StringUtils.isEmpty(property)) {
                continue;
            }
            // 上级属性为空时（绑定一级属性）
            if (StringUtils.isEmpty(parentProperty) && !property.contains(DOT)) {
                Class<?> classByProperty = getClassByClassProperty(resultType, property);
                nestedResultMaps.put(property, createResultMap(mapper, joins, classByProperty, leftJoin, property));
            }
            // 上级属性不为空时（二级属性绑定其下的直接属性）
            if (!StringUtils.isEmpty(parentProperty) && property.startsWith(parentProperty + DOT)
                    && property.replaceFirst(parentProperty + DOT, "").indexOf(DOT) == -1
            ) {
                Class<?> classByProperty = getClassByClassProperty(resultType, property);
                nestedResultMaps.put(property, createResultMap(mapper, joins, classByProperty, leftJoin, property));
            }
        }
        if (nestedResultMaps.isEmpty()) {
            log.info("--> Added ResultMap: {}, ResultType: {}", resultMap.getId(), resultType.getTypeName());
            log.info("--> ResultMapping: {}", resultMap.getResultMappings());
            return resultMap;
        }

        List<ResultMapping> resultMappings = new ArrayList<>(resultMap.getResultMappings().size() + nestedResultMaps.size());
        resultMappings.addAll(resultMap.getResultMappings());

        Set<Map.Entry<String, ResultMap>> entries = nestedResultMaps.entrySet();
        for (Map.Entry<String, ResultMap> entry : entries) {
            String property = entry.getKey();
            if (entry.getKey().indexOf(DOT) > 0) {
                property = entry.getKey().substring(entry.getKey().lastIndexOf(DOT) + 1);
            }
            String columnPrefix;
            if (StringUtils.isEmpty(parentProperty)) {
                columnPrefix = entry.getKey() + DOT;
            } else {
                columnPrefix = entry.getKey().replaceFirst(parentProperty + "\\.", "") + DOT;
            }
//            String column = getAnnotationAttributeValue(currentJoinAnnotation, "column", String.class);
            ResultMapping resultMapping = builderAssistant.buildResultMapping(
                    resultType, property, null, entry.getValue().getType(), null, null,
                    entry.getValue().getId(), null, columnPrefix, null,
                    Collections.EMPTY_LIST, null, null, false);
            resultMappings.add(resultMapping);
        }

        String id = MYBATIS_PLUS + UNDERSCORE + builderAssistant.getCurrentNamespace().replaceAll("\\.", UNDERSCORE)
                + UNDERSCORE + mapperClass.getName().replaceAll("\\.", UNDERSCORE) + UNDERSCORE + parentProperty.replaceAll("\\.", UNDERSCORE);

        log.info("--> Added ResultMap: {}, ResultType: {}", resultMap.getId(), resultType.getTypeName());
        log.info("--> ResultMapping: {}", resultMap.getResultMappings());

        ResultMapResolver resultMapResolver = new ResultMapResolver(
                builderAssistant, id, resultType, null, null, resultMappings, null
        );
        return resultMapResolver.resolve();
    }


    /**
     * @param resultType
     * @param mapperClass
     * @param excludeProperties
     * @return
     */
    ResultMap getOrNewResultMapFromClass(Class<?> resultType, Class<?> mapperClass, List<String> excludeProperties) {

        TableName tableNameAnnotation = resultType.getAnnotation(TableName.class);
        if (tableNameAnnotation != null) {
            TableInfo tableInfo = initTableInfo(builderAssistant.getConfiguration(), mapperClass, resultType);
            if (!tableInfo.isAutoInitResultMap()) {
                throw new RuntimeException(resultType.getName() + "，@TableName必须开启autoResultMap");
            }
            return builderAssistant.getConfiguration().getResultMap(tableInfo.getResultMap());
        }

        String resultMapId = MYBATIS_PLUS + UNDERSCORE + resultType.getName();
        if (builderAssistant.getConfiguration().hasResultMap(resultMapId)) {
            return builderAssistant.getConfiguration().getResultMap(resultMapId);
        }

        List<ResultMapping> resultMappings = new ArrayList<>();
        Field[] declaredFields = resultType.getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                if (excludeProperties != null && excludeProperties.contains(field.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), resultType);
                if (pd.getReadMethod() == null || pd.getWriteMethod() == null) {
                    continue;
                }
                ResultMapping resultMapping = new ResultMapping.Builder(
                        configuration, field.getName(),
                        StringUtils.camelToUnderline(field.getName()),
                        field.getType()
                ).build();
                resultMappings.add(resultMapping);
            } catch (IntrospectionException e) {
                log.warn(e.getMessage());
            } catch (Exception e) {
                log.warn(e.getMessage());
            }

        }

        ResultMap resultMap = new ResultMap.Builder(configuration, resultMapId, resultType, resultMappings).build();
        configuration.addResultMap(resultMap);
        return resultMap;
    }

    /**
     * @param configuration
     * @param mapperClass
     * @param entity
     * @return
     */
    TableInfo initTableInfo(Configuration configuration, Class<?> mapperClass, Class<?> entity) {

        MapperBuilderAssistant myBuilderAssistant = new MapperBuilderAssistant(configuration, entity.getName());
        myBuilderAssistant.setCurrentNamespace(mapperClass.getName());
        return TableInfoHelper.initTableInfo(myBuilderAssistant, entity);
    }

    /**
     * @param annotation
     * @param attribute
     * @param valueClass
     * @param <T>
     * @return
     */
    private <T> T getAnnotationAttributeValue(Annotation annotation, String attribute, Class<T> valueClass) {
        if (annotation == null) {
            return null;
        }
        Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
        return valueClass.cast(attributes.get(attribute));
    }


    /**
     * 取消逻辑删除
     *
     * @param tableInfo
     * @return
     */
    TableInfo unsetLoginDelete(TableInfo tableInfo) {
        try {
            Field logicDelete = tableInfo.getClass().getDeclaredField("logicDelete");
            logicDelete.setAccessible(true);
            logicDelete.set(tableInfo, false);
        } catch (Exception e) {
            log.warn("{}", e);
        }

        return tableInfo;
    }

    /**
     * 获取类中指定属性名的类型（如果是集合类型，则返回泛型）
     *
     * @param parentClass
     * @param propertyName
     * @return
     */
    public Class<?> getClassByClassProperty(Class<?> parentClass, String propertyName) {
        try {
            if (propertyName.indexOf(DOT) > 0) {
                propertyName = propertyName.substring(propertyName.lastIndexOf(DOT) + 1);
            }
            Field field = parentClass.getDeclaredField(propertyName);
            if (isCollectionByClassProperty(parentClass, propertyName) ||
                    IPage.class.isAssignableFrom(field.getType()) ||
                    Optional.class.isAssignableFrom(field.getType())
            ) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                return ((Class<?>) genericType.getActualTypeArguments()[0]);
            } else {
                return field.getType();
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(builderAssistant.getCurrentNamespace() + "--" + parentClass.getName() + " not property " + propertyName);
        }
    }

    /**
     * @param parentClass
     * @param propertyName
     * @return
     */
    public boolean isCollectionByClassProperty(Class<?> parentClass, String propertyName) {
        try {
            if (propertyName.indexOf(DOT) > 0) {
                propertyName = propertyName.substring(propertyName.lastIndexOf(DOT) + 1);
            }
            Field field = parentClass.getDeclaredField(propertyName);
            return Collection.class.isAssignableFrom(field.getType());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(builderAssistant.getCurrentNamespace() + "--" + parentClass.getName() + " not property " + propertyName);
        }
    }

//    测试泛型
//    private Map<String, Integer> aa;
//    public static void main(String[] args) throws NoSuchFieldException, ClassNotFoundException {
//        Field aa = JoinsMethod.class.getDeclaredField("aa");
//        ParameterizedType genericType = (ParameterizedType) aa.getGenericType();
//        Class<?> aClass = (Class<?>) genericType.getActualTypeArguments()[1];
//        System.out.println(aClass == Integer.class);
//    }

}
