package ru.ltmanagement.settings.service;

import ru.ltmanagement.settings.NSqlConfigDto;

public interface NSqlConfigService {
    NSqlConfigDto getNSqlValue(String configKey);
}
