package com.ymyang.framework.mybatisplus;


import com.ymyang.framework.mybatisplus.annotation.ManyToMany;
import com.ymyang.framework.mybatisplus.annotation.OneToMany;
import com.ymyang.framework.mybatisplus.annotation.OneToOne;
import com.ymyang.framework.mybatisplus.utils.MybatisUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseModel<T extends Model<?>> extends Model<T> {

    // 如果当前类名修改，需修改此处
    private static final Class<BaseModel> baseModelClass = BaseModel.class;


    private PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String name) throws IntrospectionException {
        return new PropertyDescriptor(name, clazz);
    }


    public static <T extends BaseModel> void loadRelations(T model) {
        loadRelations(model, new ArrayList());
    }

    public static <T extends BaseModel> void loadRelations(T model, List<String> includes) {
        loadRelations(model, null, includes);
    }

    /**
     * @param model
     * @param queryWrapperMap Map's key为关联属性名
     * @param <T>
     */
    public static <T extends BaseModel, R extends AbstractWrapper> void loadRelations(T model,
                                                                                      Map<String, R> queryWrapperMap) {
        loadRelations(model, queryWrapperMap, new ArrayList());
    }

    /**
     * @param model
     * @param queryWrapperMap
     * @param includes        如果指定包含，级联下的属性必须明确指明， 如：user.role, user.article, user.article.message
     * @param <T>
     * @param <R>
     */
    public static <T extends BaseModel, R extends AbstractWrapper> void loadRelations(T model,
                                                                                      Map<String, R> queryWrapperMap,
                                                                                      List<String> includes) {
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.add(model);
        loadRelations(arrayList, queryWrapperMap, includes);

    }


    /**
     * @param model
     * @param queryWrapperMap Map's key为关联属性名
     * @param <T>
     */
    public static <T extends BaseModel, R extends AbstractWrapper> void loadRelations(IPage<T> model,
                                                                                      Map<String, R> queryWrapperMap) {
        loadRelations(model.getRecords(), queryWrapperMap);
    }

    /**
     * @param model
     * @param queryWrapperMap
     * @param includes        如果指定包含，级联下的属性必须明确指明， 如：user.role, user.article, user.article.message
     * @param <T>
     * @param <R>
     */
    public static <T extends BaseModel, R extends AbstractWrapper> void loadRelations(IPage<T> model,
                                                                                      Map<String, R> queryWrapperMap,
                                                                                      List<String> includes) {
        loadRelations(model.getRecords(), queryWrapperMap, includes);
    }

    public static <T extends BaseModel> void loadRelations(IPage<T> models) {
        loadRelations(models.getRecords(), new ArrayList());
    }

    /**
     * @param models
     * @param includes 如果指定包含，级联下的属性必须明确指明， 如：user.role, user.article, user.article.message
     * @param <T>
     */
    public static <T extends BaseModel> void loadRelations(IPage<T> models, List<String> includes) {
        loadRelations(models.getRecords(), includes);
    }

    /**
     * @param models
     * @param queryWrapperMap Map's key为关联属性名
     * @param <T>
     */
    public static <T extends BaseModel, R extends AbstractWrapper> void loadRelations(List<T> models,
                                                                                      Map<String, R> queryWrapperMap) {
        loadRelations(models, queryWrapperMap, new ArrayList());
    }

    public static <T extends BaseModel> void loadRelations(List<T> models) {
        loadRelations(models, new ArrayList());
    }

    /**
     * @param models
     * @param includes 如果指定包含，级联下的属性必须明确指明， 如：user.role, user.article, user.article.message
     * @param <T>
     */

    public static <T extends BaseModel> void loadRelations(List<T> models, List<String> includes) {
        loadRelations(models, null, includes);
    }

    /**
     * @param models
     * @param queryWrapperMap
     * @param includes        如果指定包含，级联下的属性必须明确指明， 如：user.role, user.article, user.article.message
     * @param <T>
     * @param <R>
     */
    public static <T extends BaseModel, R extends AbstractWrapper> void loadRelations(List<T> models,
                                                                                      Map<String, R> queryWrapperMap,
                                                                                      List<String> includes) {
        if (models.isEmpty()) {
            return;
        }
        Class<? extends BaseModel> modelClass = models.get(0).getClass();
        Map<String, Annotation> relationFields = getRelationFields(modelClass);
        doLoadRelations(relationFields, models, queryWrapperMap, includes);
    }

    /**
     * @param relationFields
     * @param models
     * @param queryWrapperMap
     * @param includes        如果指定包含，级联下的属性必须明确指明， 如：user.role, user.article, user.article.message
     * @param <T>
     * @param <R>
     */
    private static <T extends BaseModel, R extends AbstractWrapper> void doLoadRelations(
            Map<String, Annotation> relationFields,
            List<T> models,
            Map<String, R> queryWrapperMap,
            List<String> includes
    ) {

        if (relationFields.isEmpty()) {
            return;
        }

        // 级联包含处理， 如：user.role, user.article, user.article.message
        List<String> newIncludes;
        if (includes.isEmpty()) {
            newIncludes = new ArrayList();
        } else {
            newIncludes = includes.stream().map(include -> {
                String[] splitedInclude = include.split("\\.", 2);
                if (splitedInclude.length > 1) {
                    return splitedInclude[1];
                }
                return "";
            }).distinct().collect(Collectors.toList());
        }

        for (Map.Entry<String, Annotation> entry : relationFields.entrySet()) {
            String fieldName = entry.getKey();
            Annotation annotation = entry.getValue();

            if (!includes.isEmpty() && !includes.contains(fieldName)) {
                continue;
            }

            String localKey;
            String foreignKey;
            Class relationClass;
            boolean isOneToOne = false;
            boolean isOneToMany = false;
            boolean isManyToMany = false;
            if (OneToOne.class.isAssignableFrom(annotation.getClass())) {
                localKey = ((OneToOne) annotation).localKey();
                foreignKey = ((OneToOne) annotation).value();
                relationClass = ((OneToOne) annotation).relation();
                isOneToOne = true;
            } else if (OneToMany.class.isAssignableFrom(annotation.getClass())) {
                localKey = ((OneToMany) annotation).localKey();
                foreignKey = ((OneToMany) annotation).value();
                relationClass = ((OneToMany) annotation).relation();
                isOneToMany = true;
            } else {
                localKey = ((ManyToMany) annotation).localKey();
                foreignKey = ((ManyToMany) annotation).value();
                relationClass = ((ManyToMany) annotation).relationModel();
                isManyToMany = true;
            }
            PropertyDescriptor readPropertyDesc = null;
            PropertyDescriptor writePropertyDesc = null;
            PropertyDescriptor joinPropertyDesc = null;
            Method readMethod = null;
            Method writeMethod = null;
            Method joinReadMethod = null;
            Set<Object> inList = new HashSet<Object>(models.size());
            try {
                readPropertyDesc = new PropertyDescriptor(StringUtils.underlineToCamel(localKey), models.get(0).getClass());
                writePropertyDesc = new PropertyDescriptor(fieldName, models.get(0).getClass());
                joinPropertyDesc = new PropertyDescriptor(StringUtils.underlineToCamel(foreignKey), relationClass);
                readMethod = readPropertyDesc.getReadMethod();
                writeMethod = writePropertyDesc.getWriteMethod();
                joinReadMethod = joinPropertyDesc.getReadMethod();
                for (T model : models) {
                    inList.add(readMethod.invoke(model));
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                return;
            }

            QueryWrapper queryWrapper;
            if (queryWrapperMap != null && queryWrapperMap.get(fieldName) != null) {
                queryWrapper = (QueryWrapper) queryWrapperMap.get(fieldName);
            } else {
                queryWrapper = Wrappers.query();
            }

            // 查询关联表的数据
            List subList;
            if (isManyToMany) {
                ManyToMany manyToMany = (ManyToMany) annotation;
                BaseModel relationModel = BeanUtils.instantiateClass(manyToMany.pivotModel(), BaseModel.class);
                String on = manyToMany.pivotForeignKey() + " = t2." + manyToMany.value();
                queryWrapper.in(manyToMany.pivotLocalKey(), inList);
                if (manyToMany.pivotColumns().length > 0) {
                    queryWrapper.select(manyToMany.pivotColumns());
                } else {
                    queryWrapper.select("t1." + manyToMany.pivotLocalKey(), "t2.*");
                }
                queryWrapper.apply(!StringUtils.isEmpty(manyToMany.condition()), manyToMany.condition());
                subList = relationModel.leftJoin(
                        manyToMany.relationModel(),
                        on,
                        queryWrapper,
                        manyToMany.resultType()
                );
            } else {
                BaseModel relationModel = BeanUtils.instantiateClass(relationClass, BaseModel.class);
                queryWrapper.in(foreignKey, inList);
                subList = relationModel.selectList(queryWrapper);
            }

            // 如果关联表的数据为空
            if (subList.isEmpty()) {
                for (T model : models) {
                    if (List.class.isAssignableFrom(writePropertyDesc.getPropertyType())) {
                        try {
                            writeMethod.invoke(model, new ArrayList());
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }

            if (isOneToOne) { // 一对一
                for (T model : models) {
                    for (Object item : subList) {
                        try {
                            Object subVal = joinReadMethod.invoke(item);
                            if (readMethod.invoke(model).equals(subVal)) {
                                writeMethod.invoke(model, item);
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }
                }
                BaseModel.loadRelations(subList, queryWrapperMap, newIncludes);

            } else if (isOneToMany) { // 一对多

                for (T model : models) {
                    try {
                        ArrayList<Object> exactSubList = new ArrayList();
                        for (Object item : subList) {
                            Object subVal = joinReadMethod.invoke(item);
                            if (readMethod.invoke(model).equals(subVal)) {
                                exactSubList.add(item);
                            }
                        }
                        writeMethod.invoke(model, exactSubList);
                    } catch (Exception e) {
                    }
                }
                BaseModel.loadRelations(subList, queryWrapperMap, newIncludes);

            } else if (isManyToMany) { // 多对多

                ManyToMany manyToMany = (ManyToMany) annotation;
                for (T model : models) {
                    ArrayList<Object> exactSubList = new ArrayList();
                    // 查询结果空
                    if (subList.isEmpty()) {
                        try {
                            writeMethod.invoke(model, exactSubList);
                        } catch (Exception e) {
                        }
                        continue;
                    }

                    PropertyDescriptor propertyDescriptor; // 中间表中，主表属性
                    Object keyValue; // 主表 主键值
                    try {
                        keyValue = readMethod.invoke(model);
                        propertyDescriptor = new PropertyDescriptor(
                                StringUtils.underlineToCamel(manyToMany.pivotLocalKey()),
                                subList.get(0).getClass()
                        );
                    } catch (Exception e) {
                        continue;
                    }
                    Method propertyReadMethod = propertyDescriptor.getReadMethod();
                    for (Object item : subList) {
                        try {
                            if (keyValue != null && keyValue.equals(propertyReadMethod.invoke(item))) {
                                exactSubList.add(item);
                            }
                        } catch (Exception e) {
                        }
                    }
                    try {
                        writeMethod.invoke(model, exactSubList);
                    } catch (Exception e) {
                    }
                }
                BaseModel.loadRelations(subList, queryWrapperMap, newIncludes);
            }
        }
    }


    /**
     * 左外连接（默认会给表加别名：t1、t2...）
     * 支持多表join
     * <p>
     * EXAMPLE:
     * adminEntity.leftJoin(
     * Lists.newArrayList(AdminRoleEntity.class, RoleEntity.class),
     * Lists.newArrayList("t1.id=t2.admin_id", "t2.role_id=t3.id"),
     * Wrappers.query(),
     * RoleEntity.class
     * );
     * </p>
     *
     * @param joinModels   join表
     * @param onCondition  on条件
     * @param queryWrapper where条件
     * @param resultClass  返回的结果类
     * @param <R>          返回的结果类型
     * @return
     */
    public <R> List<R> leftJoin(List<Class> joinModels, List<String> onCondition, AbstractWrapper queryWrapper, Class<R> resultClass) {
        if (joinModels.size() != onCondition.size()) {
            throw new IllegalArgumentException("关联表的数量与ON条件数量不一致！");
        }
        StringBuilder modelNameStringBuilder = new StringBuilder();
        StringBuilder tableStringBuilder = new StringBuilder(SqlHelper.table(getClass()).getTableName());
        tableStringBuilder.append(" t1 ");
        for (int i = 1; i <= joinModels.size(); i++) {
            Class model = joinModels.get(i - 1);
            modelNameStringBuilder.append(model.getSimpleName());
            tableStringBuilder.append(" LEFT JOIN ");
            tableStringBuilder.append(SqlHelper.table(model).getTableName());
            tableStringBuilder.append(" t");
            tableStringBuilder.append(i + 1);
            tableStringBuilder.append(" ON ");
            tableStringBuilder.append(onCondition.get(i - 1));
        }
        String mapperId = getClass().getName() + ".join" + modelNameStringBuilder.toString() + "MappedStatement():" + resultClass.getSimpleName();
        SqlSession sqlSession = SqlHelper.sqlSession(getClass());
        MybatisUtil.createCommonMappedStatementIfNeed(sqlSession, mapperId, resultClass);

        HashMap<String, Object> map = new HashMap<String, Object>(3);
        String sqlSelect = queryWrapper.getSqlSelect();
        map.put("columns", sqlSelect == null || "".equals(sqlSelect) ? "*" : sqlSelect);
        map.put("table", tableStringBuilder.toString());
        map.put(Constants.WRAPPER, queryWrapper);
        // "SELECT ${columns} FROM ${table} ${where}"
        try {
            List<Object> result = sqlSession.selectList(mapperId, map);
            return (List<R>) result;
        } finally {
            closeSqlSession(sqlSession);
        }
    }

    /**
     * 左外连接（默认会给表加别名：t1、t2）
     * <p>
     * EXAMPLE:
     * adminEntity.leftJoin(
     * AdminRoleEntity.class,
     * "t1.id=t2.admin_id",
     * Wrappers.query(),
     * RoleEntity.class
     * );
     * </p>
     *
     * @param joinModels
     * @param on
     * @param queryWrapper
     * @param resultClass
     * @param <R>
     * @return
     */
    public <R> List<R> leftJoin(Class joinModels, String on, AbstractWrapper queryWrapper, Class<R> resultClass) {
        ArrayList<Class> models = new ArrayList<>();
        models.add(joinModels);
        ArrayList<String> onList = new ArrayList<>();
        onList.add(on);
        return leftJoin(models, onList, queryWrapper, resultClass);
    }


    /**
     * 获取字段中含有注解OneToOne OneToMany ManyToMany
     *
     * @param modelClass
     * @return
     */
    private static Map<String, Annotation> getRelationFields(Class<? extends BaseModel> modelClass) {

        HashMap<String, Annotation> fieldMappingAnnotation = new HashMap();
        for (Field field : modelClass.getDeclaredFields()) {
            OneToOne oneToOne = field.getAnnotation(OneToOne.class);
            if (oneToOne != null) {
                fieldMappingAnnotation.put(field.getName(), oneToOne);
                continue;
            }
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (oneToMany != null) {
                fieldMappingAnnotation.put(field.getName(), oneToMany);
                continue;
            }
            ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
            if (manyToMany != null) {
                fieldMappingAnnotation.put(field.getName(), manyToMany);
                continue;
            }
        }
        return fieldMappingAnnotation;
    }

}
