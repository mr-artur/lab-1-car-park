package ua.kpi.fict.carpark.mapper;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class LocalizedMapper<E, T> {

    public T toDto(E entity) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.equals(Locale.ENGLISH)) {
            return toDtoWithDefaultData(entity);
        }
        return toDtoWithNativeData(entity);
    }

    public List<T> toDtos(Collection<E> entities) {
        return entities.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    protected abstract T toDtoWithDefaultData(E entity);

    protected abstract T toDtoWithNativeData(E entity);
}