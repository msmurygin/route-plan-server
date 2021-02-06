package ru.ltmanagement.settings.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingsDto {
    private boolean hideNotFinishedTask;//не отображать не завершенные задачи
    private boolean showLastShiftNotFinishedTask;//отображать не завершенные задачи за прошлую смену
    private boolean showNotFinishedTaskInPeriod;//отображать не завершенные задачи за заданый промежуток
    private boolean showAllNotFinishedTask;//отображать все не завершенные задачи
    private boolean hidePlanedTasks;//не отображать планируемые задачи
    private boolean showNextShiftPlanedTasks;//отображать планируемые задачи за следующую смену
    private boolean showPlanedTaskInPeriod;//отображать планируемые задачи за заданый промежуток времени
    private int dayShiftBegin;//начало дневной смены
    private int dayShiftEnd;//конец дневной смены
    private int nightShiftBegin;//начало ночной смены
    private int nightShiftEnd;//конец ночной смены
}
