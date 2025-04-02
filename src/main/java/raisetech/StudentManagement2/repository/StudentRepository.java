package raisetech.StudentManagement2.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student>search();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses>searchStudentsCourses();

  @Insert("insert into students(name, kana_name, nickname, email, area, age, sex, remark)"
      + " values(#{name}, #{kanaName}, #{nickname}, #{email}, #{area}, #{age}, #{sex}, #{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);
}
