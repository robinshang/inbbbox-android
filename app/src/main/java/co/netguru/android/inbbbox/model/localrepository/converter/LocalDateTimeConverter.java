package co.netguru.android.inbbbox.model.localrepository.converter;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.threeten.bp.LocalDateTime;

public class LocalDateTimeConverter implements PropertyConverter<LocalDateTime, String> {

   @Override
    public LocalDateTime convertToEntityProperty(String databaseValue) {
        return LocalDateTime.parse(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(LocalDateTime entityProperty) {
        return entityProperty.toString();
    }
}
