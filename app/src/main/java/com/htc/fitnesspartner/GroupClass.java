package com.htc.fitnesspartner;

import java.util.List;

public class GroupClass {

    private String name, description, coach;
    private Long createDate;
    private List<String> workouts, athletes;

    public List<String> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<String> athletes) {
        this.athletes = athletes;
    }

    public GroupClass() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public List<String> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<String> workouts) {
        this.workouts = workouts;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public GroupClass(String name, String description, Long createDate, List<String> workouts, List<String> athletes, String coach) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.workouts = workouts;
        this.athletes = athletes;
        this.coach = coach;
    }
}
