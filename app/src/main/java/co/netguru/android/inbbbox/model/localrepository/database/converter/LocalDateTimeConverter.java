package co.netguru.android.inbbbox.model.localrepository.database.converter;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.threeten.bp.LocalDateTime;

public class LocalDateTimeConverter implements PropertyConverter<LocalDateTime, String> {

    // TODO: 21.12.2016 Change to ZonedDateTime
   @Override
    public LocalDateTime convertToEntityProperty(String databaseValue) {
        return LocalDateTime.parse(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(LocalDateTime entityProperty) {
        return entityProperty.toString();
    }
}
