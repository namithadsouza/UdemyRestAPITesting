package org.example.pojo;

import lombok.Data;

@Data
public class UdemyCourseResponse {
    private String instructor;
    private String url;
    private String services;
    private String expertise;
    private Courses courses;
    private String linkedIn;
}
