package com.epam;

public enum AppSettings {
    PERSISTENCE_UNIT("test");

    private String settingValue;

    AppSettings(String settingValue) {
        this.settingValue = settingValue;
    }

    public String getSettingValue() {
        return settingValue;
    }
}
