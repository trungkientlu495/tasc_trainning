package ntk.project.dto;

import java.time.LocalDateTime;

public class SearchLog {
    private String logName;
    private String startTimeLogSearch;
    private String endTimeLogSearch;
    private String servicesName;
    private String logMessage;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getStartTimeLogSearch() {
        return startTimeLogSearch;
    }

    public void setStartTimeLogSearch(String startTimeLogSearch) {
        this.startTimeLogSearch = startTimeLogSearch;
    }

    public String getEndTimeLogSearch() {
        return endTimeLogSearch;
    }

    public void setEndTimeLogSearch(String endTimeLogSearch) {
        this.endTimeLogSearch = endTimeLogSearch;
    }

    public String getServicesName() {
        return servicesName;
    }

    public void setServicesName(String servicesName) {
        this.servicesName = servicesName;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
