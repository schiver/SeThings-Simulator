package com.example.schiver.projectdua.Model;

public class DeviceUsageData {
    private int icon;
    private String deviceID,deviceType,deviceName,deviceInfo;
    private float totalUsage;

    public DeviceUsageData() {
    }

    public DeviceUsageData(int icon, String deviceID, String deviceType, String deviceName, String deviceInfo, float totalUsage) {
        this.icon = icon;
        this.deviceID = deviceID;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.deviceInfo = deviceInfo;
        this.totalUsage = totalUsage;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public float getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(float totalUsage) {
        this.totalUsage = totalUsage;
    }
}
