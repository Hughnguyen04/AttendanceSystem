package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingDTO {

    private Integer id;
    private String settingKey;
    private String settingValue;
    private String description;
    private String settingType;
}
