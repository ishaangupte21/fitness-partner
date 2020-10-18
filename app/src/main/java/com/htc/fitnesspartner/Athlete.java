package com.htc.fitnesspartner;

import java.util.ArrayList;
import java.util.List;

public class Athlete {
    private String firstName, lastName, emailAddress;
    private Long dateJoined;
    private List<String> enrolledClasses, workoutsCompleted;
    private Integer completedSize;


    public Athlete(){}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Long dateJoined) {
        this.dateJoined = dateJoined;
    }

    public List<String> getEnrolledClasses() {
        return enrolledClasses;
    }

    public void setEnrolledClasses(List<String> enrolledClasses) {
        this.enrolledClasses = enrolledClasses;
    }

    public List<String> getWorkoutsCompleted() {
        return workoutsCompleted;
    }

    public void setWorkoutsCompleted(List<String> workoutsCompleted) {
        this.workoutsCompleted = workoutsCompleted;
    }

    public Integer getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(Integer completedSize) {
        this.completedSize = completedSize;
    }

    public Athlete(String firstName, String lastName, String emailAddress, Long dateJoined, List<String> enrolledClasses, List<String> workoutsCompleted, Integer completedSize) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.dateJoined = dateJoined;
        this.enrolledClasses = enrolledClasses;
        this.workoutsCompleted = workoutsCompleted;
        this.completedSize = completedSize;
    }
}
