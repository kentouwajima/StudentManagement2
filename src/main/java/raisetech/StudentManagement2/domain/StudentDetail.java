package raisetech.StudentManagement2.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentsCourses;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentsCourses> studentsCourses;
}
