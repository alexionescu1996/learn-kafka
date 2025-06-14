package model;

import java.io.Serializable;

public class Alert
        implements Serializable {

    private final int alertId;
    private String stageId;
    private final String alertLevel;
    private final String alertMessage;

    public Alert(int alertId, String stageId,
                 String alertLevel, String alertMessage) {

        this.alertLevel = alertLevel;
        this.alertId = alertId;
        this.stageId = stageId;
        this.alertMessage = alertMessage;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageId() {
        return stageId;
    }

    public int getAlertId() {
        return alertId;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public String getAlertMessage() {
        return alertMessage;
    }
}
