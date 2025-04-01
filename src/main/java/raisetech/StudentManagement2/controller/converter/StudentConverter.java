package raisetech.StudentManagement2.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentsCourses;
import raisetech.StudentManagement2.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentsCourses> studentsCoursesList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentsCourses> convertStudentsCoursesList = studentsCoursesList.stream()
          .filter(studentCourse -> student.getId() == (studentCourse.getStudentId()))
          .collect(Collectors.toList());

      studentDetail.setStudentsCourses(convertStudentsCoursesList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }
}
