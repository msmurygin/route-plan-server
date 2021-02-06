package ru.ltmanagement.settings.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.ltmanagement.settings.NSqlConfigDto;

@Getter
@Setter
@RequiredArgsConstructor
public class NSqlConfigResponse {
    private final NSqlConfigDto nsqConfig;
}
