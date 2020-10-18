package com.htc.fitnesspartner;

import java.util.List;

public class Workout {

    private String name, exercise, parentClass;
    private int amount, reps;
    private long date;
    private List<String> completedBy;

    public List<String> getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(List<String> completedBy) {
        this.completedBy = completedBy;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Workout(){}

    public Workout(String name, String exercise, int amount, int reps, long date, String parentClass, List<String> completedBy) {
        this.name = name;
        this.exercise = exercise;
        this.amount = amount;
        this.reps = reps;
        this.date = date;
        this.parentClass = parentClass;
        this.completedBy = completedBy;
    }
}
