package ru.ltmanagement.settings;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NSqlConfigDto {
    private String nSqlValue;
    private String nSqlDescript;
}
