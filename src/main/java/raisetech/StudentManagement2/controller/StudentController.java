package raisetech.StudentManagement2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import raisetech.StudentManagement2.controller.converter.StudentConverter;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentsCourses;
import raisetech.StudentManagement2.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model){
    List<Student> students =  service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();
    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }

  @GetMapping("/studentsCoursesList")
  public List<StudentsCourses> getStudentsCoursesList(){
    return service.searchStudentsCoursesList();
  }
}
