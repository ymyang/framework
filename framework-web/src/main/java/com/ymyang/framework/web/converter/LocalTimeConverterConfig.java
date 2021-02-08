package com.ymyang.framework.web.converter;

import com.ymyang.framework.web.constant.ExceptionConstant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 全局页面传入日期字符串，自动转换成日期格式
 */
@Component
public class LocalTimeConverterConfig implements Converter<String, LocalTime> {

    private static final List<DateTimeFormatter> formarts = new ArrayList<>(2);

    static {
        formarts.add(DateTimeFormatter.ofPattern("H:m"));
        formarts.add(DateTimeFormatter.ofPattern("H:m:s"));
    }

    public LocalTimeConverterConfig() {
    }

    @Override
    public LocalTime convert(String source) {

        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }

        try {
            if (source.matches("^\\d{1,2}:\\d{1,2}$")) {
                return LocalTime.parse(source, formarts.get(0));
            } else if (source.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return LocalTime.parse(source, formarts.get(1));
            }
        }catch (Exception e){

        }

        throw new IllegalArgumentException(ExceptionConstant.SEPRATER + "无效的日期格式 '" + source + "'");
    }

    /**
     * 格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    public Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }

}
