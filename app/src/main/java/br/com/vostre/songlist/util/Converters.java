package br.com.vostre.songlist.util;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class Converters {

    @TypeConverter
    public static DateTime fromTimestamp(Long value) {

        if(value != null){
            DateTime dateTime = new DateTime(value,
                    DateTimeZone.forTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")));
            //dateTime.withDate(new LocalDate(value));

            return dateTime;
        } else{
            return null;
        }

    }

    @TypeConverter
    public static Long dateTimeToTimestamp(DateTime dateTime) {
        return dateTime == null ? null : dateTime.getMillis();
    }

}
