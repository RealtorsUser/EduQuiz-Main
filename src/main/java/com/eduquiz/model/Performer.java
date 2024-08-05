package com.eduquiz.model;

public class Performer {

    private String quizId;
    private String name;
    private String schoolName;
    private int marks;
    private String coupon;

    public Performer(String quizId, String name, String schoolName, int marks, String coupon) {
        this.quizId = quizId;
        this.name = name;
        this.schoolName = schoolName;
        this.marks = marks;
        this.coupon = coupon;
    }

    // Getters and setters

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}

