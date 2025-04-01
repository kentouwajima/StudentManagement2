package raisetech.StudentManagement2.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student>search();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse>searchStudentCourse();
}
