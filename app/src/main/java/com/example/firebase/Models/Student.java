package com.example.firebase.Models;

public class Student {

    public String name;
    public int age;
    public String collage ;
    public String branch;

    public Student() {
    }

    public Student(String name, int age, String collage, String branch) {
        this.name = name;
        this.age = age;
        this.collage = collage;
        this.branch = branch;
    }
}
