package raisetech.StudentManagement2.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentsCourses;
import raisetech.StudentManagement2.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList(){
    return repository.search();
  }

  public List<StudentsCourses> searchStudentsCoursesList(){
    return repository.searchStudentsCourses();
  }
}
