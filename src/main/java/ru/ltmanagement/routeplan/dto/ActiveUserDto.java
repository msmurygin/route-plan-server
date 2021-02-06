package ru.ltmanagement.routeplan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ActiveUserDto {
    private int mulinet;
    private int forkLift;
    private int radioDevices;
}
