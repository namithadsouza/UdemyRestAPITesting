package org.example.pojo;

import lombok.Data;

@Data
public class Course {
    private String courseTitle;
    private String price;

    /* Constructor
    public Course() {
    }

    public Course(String courseTitle, String price) {
        this.courseTitle = courseTitle;
        this.price = price;
    }

    // Getters and Setters
    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseTitle='" + courseTitle + '\'' +
                ", price='" + price + '\'' +
                '}';
    }*/
}
