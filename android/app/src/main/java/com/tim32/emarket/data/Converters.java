package com.tim32.emarket.data;

import androidx.room.TypeConverter;
import com.tim32.emarket.data.entity.EntityType;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static EntityType entityTypeFromName(String name) {
        return EntityType.valueOf(name);
    }

    @TypeConverter
    public static String entityTypeToName(EntityType entityType) {
        return entityType.name();
    }
}
