package raisetech.StudentManagement2.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentCourse;
import raisetech.StudentManagement2.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList(){
    return repository.search()
        .stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .collect(Collectors.toList());
  }

  public List<StudentCourse> searchStudentCourseList(){
    return repository.searchStudentCourse()
        .stream()
        .filter(course -> "Javaコース".equals(course.getCourseName()))
        .collect(Collectors.toList());
  }
}
