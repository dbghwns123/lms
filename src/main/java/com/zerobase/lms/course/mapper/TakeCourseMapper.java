package com.zerobase.lms.course.mapper;

import com.zerobase.lms.course.dto.CourseDto;
import com.zerobase.lms.course.dto.TakeCourseDto;
import com.zerobase.lms.course.model.CourseParam;
import com.zerobase.lms.course.model.TakeCourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TakeCourseMapper {

    long selectListCount(TakeCourseParam parameter);
    List<TakeCourseDto> selectList(TakeCourseParam parameter);

    // 추가
    List<TakeCourseDto> selectListMyCourse(TakeCourseParam parameter);
}
