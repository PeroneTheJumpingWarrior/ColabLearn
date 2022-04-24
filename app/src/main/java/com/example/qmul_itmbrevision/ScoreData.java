package com.example.qmul_itmbrevision;

public class ScoreData {

    String name; //This Variable is for user's name
    long score; //This Variable is for obtaining user's "timeSpent" value

    public ScoreData(String name, long score) {
        this.name = name;
        this.score = score;
    }

    public ScoreData(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}