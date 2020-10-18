package com.htc.fitnesspartner;

import java.util.List;

public class Coach {
    private String firstName, lastName, emailAddress;
    private Long dateJoined;
    private List<String> classes;


    public Coach(){}

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

    public Long getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Long dateJoined) {
        this.dateJoined = dateJoined;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public Coach(String firstName, String lastName, String emailAddress, List<String> classes, Long dateJoined) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.classes = classes;
        this.dateJoined = dateJoined;
        this.emailAddress = emailAddress;
    }
}
