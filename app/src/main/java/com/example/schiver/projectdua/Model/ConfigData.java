package com.example.schiver.projectdua.Model;

public class ConfigData {
    private int icon;
    private String deviceID, deviceType,deviceName;
    private String deviceEvent, deviceCondition,getDeviceConditionConnected,deviceAction,
            deviceActionStart,deviceActionEnd;

    public ConfigData() {
    }

    public ConfigData(int icon, String deviceID, String deviceType, String deviceName, String deviceEvent, String deviceCondition, String getDeviceConditionConnected, String deviceAction, String deviceActionStart, String deviceActionEnd) {
        this.icon = icon;
        this.deviceID = deviceID;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.deviceEvent = deviceEvent;
        this.deviceCondition = deviceCondition;
        this.getDeviceConditionConnected = getDeviceConditionConnected;
        this.deviceAction = deviceAction;
        this.deviceActionStart = deviceActionStart;
        this.deviceActionEnd = deviceActionEnd;
    }

    public int getIcon() {
        return icon;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceEvent() {
        return deviceEvent;
    }

    public String getDeviceCondition() {
        return deviceCondition;
    }

    public String getGetDeviceConditionConnected() {
        return getDeviceConditionConnected;
    }

    public String getDeviceAction() {
        return deviceAction;
    }

    public String getDeviceActionStart() {
        return deviceActionStart;
    }

    public String getDeviceActionEnd() {
        return deviceActionEnd;
    }
}
