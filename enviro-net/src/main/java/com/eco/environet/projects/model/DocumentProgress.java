package com.eco.environet.projects.model;

import lombok.Getter;

@Getter
public class DocumentProgress {

    private double progress;
    private String status;

    public DocumentProgress() {
        this.progress = 0;
        updateStatus();
    }

    public void setProgress(double progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress value must be between 0 and 100.");
        }
        this.progress = progress;
        updateStatus();
    }

    private void updateStatus() {
        if (progress == 0) {
            status = "Not Started";
        } else if (progress < 50) {
            status = "In Progress - less than halfway";
        } else if (progress == 50) {
            status = "Halfway Done";
        } else if (progress > 50 && progress < 100) {
            status = "In Progress - more than halfway";
        } else {
            status = "Completed";
        }
    }

}

