package raisetech.StudentManagement2.controller.converter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentCourse;
import raisetech.StudentManagement2.domain.StudentDetail;
import raisetech.StudentManagement2.repository.StudentRepository;
import raisetech.StudentManagement2.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentConverterTest {

  @Mock
  private StudentConverter converter;

  @Mock
  private StudentRepository repository;

  @InjectMocks
  private StudentService sut;

  @Test
  void コンバータがサービス内で呼び出されること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentDetail> expected = new ArrayList<>();

    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

    sut.searchStudentList();

    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }
}

