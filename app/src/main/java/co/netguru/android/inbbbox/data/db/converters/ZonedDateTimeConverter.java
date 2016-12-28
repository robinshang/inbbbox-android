package co.netguru.android.inbbbox.data.db.converters;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.threeten.bp.ZonedDateTime;

public class ZonedDateTimeConverter implements PropertyConverter<ZonedDateTime, String> {

   @Override
    public ZonedDateTime convertToEntityProperty(String databaseValue) {
        return ZonedDateTime.parse(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(ZonedDateTime entityProperty) {
        return entityProperty.toString();
    }
}
