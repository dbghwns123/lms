package com.zerobase.lms.course.model;

import lombok.Data;

@Data
public class TakeCourseInput {

    long courseId;
    String userId;

    long takeCourseId; //take_course.id
}
