import java.io.Serializable;

public class Alert implements Serializable {

    private final int alertId;
    private String stageId;
    private final String alertLevel;
    private final String alertMessage;

    public Alert(String alertMessage, String alertLevel, String stageId, int alertId) {
        this.alertMessage = alertMessage;
        this.alertLevel = alertLevel;
        this.stageId = stageId;
        this.alertId = alertId;
    }

    public int getAlertId() {
        return alertId;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public String getAlertMessage() {
        return alertMessage;
    }
}
