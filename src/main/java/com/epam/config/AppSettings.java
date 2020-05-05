package com.epam.config;

public enum AppSettings {
    PERSISTENCE_UNIT("rent-a-car");

    private final String settingValue;

    AppSettings(String settingValue) {
        this.settingValue = settingValue;
    }

    public String getSettingValue() {
        return settingValue;
    }
}
