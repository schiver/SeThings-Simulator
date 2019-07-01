package com.example.schiver.projectdua.Model;

public class ConfigDetailData {
    String devCondition,devConditionTimerDuration,
            devConditionStartScheduled,devConditionEndScheduled;
    String devSubCondition,devSubConditioTimerDuration,
            devSubConditionStartScheduled,devSubConditionEndScheduled;

    public ConfigDetailData() {
    }

    public ConfigDetailData(String devCondition, String devConditionTimerDuration, String devConditionStartScheduled, String devConditionEndScheduled, String devSubCondition, String devSubConditioTimerDuration, String devSubConditionStartScheduled, String devSubConditionEndScheduled) {
        this.devCondition = devCondition;
        this.devConditionTimerDuration = devConditionTimerDuration;
        this.devConditionStartScheduled = devConditionStartScheduled;
        this.devConditionEndScheduled = devConditionEndScheduled;
        this.devSubCondition = devSubCondition;
        this.devSubConditioTimerDuration = devSubConditioTimerDuration;
        this.devSubConditionStartScheduled = devSubConditionStartScheduled;
        this.devSubConditionEndScheduled = devSubConditionEndScheduled;
    }

    public String getDevCondition() {
        return devCondition;
    }

    public void setDevCondition(String devCondition) {
        this.devCondition = devCondition;
    }

    public String getDevConditionTimerDuration() {
        return devConditionTimerDuration;
    }

    public void setDevConditionTimerDuration(String devConditionTimerDuration) {
        this.devConditionTimerDuration = devConditionTimerDuration;
    }

    public String getDevConditionStartScheduled() {
        return devConditionStartScheduled;
    }

    public void setDevConditionStartScheduled(String devConditionStartScheduled) {
        this.devConditionStartScheduled = devConditionStartScheduled;
    }

    public String getDevConditionEndScheduled() {
        return devConditionEndScheduled;
    }

    public void setDevConditionEndScheduled(String devConditionEndScheduled) {
        this.devConditionEndScheduled = devConditionEndScheduled;
    }

    public String getDevSubCondition() {
        return devSubCondition;
    }

    public void setDevSubCondition(String devSubCondition) {
        this.devSubCondition = devSubCondition;
    }

    public String getDevSubConditioTimerDuration() {
        return devSubConditioTimerDuration;
    }

    public void setDevSubConditioTimerDuration(String devSubConditioTimerDuration) {
        this.devSubConditioTimerDuration = devSubConditioTimerDuration;
    }

    public String getDevSubConditionStartScheduled() {
        return devSubConditionStartScheduled;
    }

    public void setDevSubConditionStartScheduled(String devSubConditionStartScheduled) {
        this.devSubConditionStartScheduled = devSubConditionStartScheduled;
    }

    public String getDevSubConditionEndScheduled() {
        return devSubConditionEndScheduled;
    }

    public void setDevSubConditionEndScheduled(String devSubConditionEndScheduled) {
        this.devSubConditionEndScheduled = devSubConditionEndScheduled;
    }
}
