package ru.ltmanagement.ordermanagement.dto;

import java.util.HashMap;
import java.util.Map;

public enum AllocationProcessingTypeResultDto {

    SYNC_OPERATION("1"),
    ASYNC_OPERATION("2");
    private String flag;

    private static final Map<String, AllocationProcessingTypeResultDto> BY_FLAG = new HashMap<>();


    static {
        for (AllocationProcessingTypeResultDto e: values()) {
            BY_FLAG.put(e.flag, e);
        }
    }


    private AllocationProcessingTypeResultDto(String flag){
        this.flag = flag;
    }

    public String getFlag() {
        return this.flag;
    }

    public static AllocationProcessingTypeResultDto valueOfFlag(String flag) {
        return BY_FLAG.get(flag);
    }
}
