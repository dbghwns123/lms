package com.zerobase.lms.course.repository;

import com.zerobase.lms.course.entity.Course;
import com.zerobase.lms.course.entity.TakeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TakeCourseRepository extends JpaRepository<TakeCourse, Long> {

    long countByCourseIdAndUserIdAndStatusIn(Long courseId, String userId, List<String> list);

}
