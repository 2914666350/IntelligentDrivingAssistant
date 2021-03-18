package com.example.intelligentdrivingassistant.data;

public class Result {
    public String status;
    public String message;

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
