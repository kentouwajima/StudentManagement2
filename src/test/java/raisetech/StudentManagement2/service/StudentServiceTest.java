package raisetech.StudentManagement2.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement2.controller.converter.StudentConverter;
import raisetech.StudentManagement2.data.CourseStatus;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentCourse;
import raisetech.StudentManagement2.data.StudentSearchCondition;
import raisetech.StudentManagement2.domain.StudentDetail;
import raisetech.StudentManagement2.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before(){
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること(){
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細の検索_リポジトリの処理が適切に呼び出せていること(){
    int studentId = 999;
    String id = String.valueOf(studentId); // ← Stringに変換

    Student student = new Student();
    student.setId(studentId); // ← intでセット

    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(studentId)).thenReturn(new ArrayList<>());

    StudentDetail expected = new StudentDetail(student, new ArrayList<>());
    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(studentId); // ← int型
    Assertions.assertEquals(expected.getStudent().getId(), actual.getStudent().getId());
  }

  @Test
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出せていること(){
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出せていること(){
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }

  @Test
  void コースステータス一覧の検索_リポジトリの処理が適切に呼び出せていること() {
    List<CourseStatus> courseStatusList = new ArrayList<>();
    when(repository.searchCourseStatuses()).thenReturn(courseStatusList);

    List<CourseStatus> result = sut.searchCourseStatuses();

    verify(repository, times(1)).searchCourseStatuses();
    Assertions.assertEquals(courseStatusList, result);
  }

  @Test
  void コースステータスの登録_リポジトリの処理が適切に呼び出せていること() {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setStudentCourseId(1);

    StudentCourse existingCourse = new StudentCourse();
    when(repository.searchStudentCourseById(1)).thenReturn(existingCourse);

    sut.registerCourseStatus(courseStatus);

    verify(repository, times(1)).searchStudentCourseById(1);
    verify(repository, times(1)).registerCourseStatus(courseStatus);
  }

  @Test
  void コースステータスの更新_リポジトリの処理が適切に呼び出せていること() {
    CourseStatus courseStatus = new CourseStatus();

    sut.updateCourseStatus(courseStatus);

    verify(repository, times(1)).updateCourseStatus(courseStatus);
  }

  @Test
  void コースステータスの削除_リポジトリの処理が適切に呼び出せていること() {
    int id = 1;

    sut.deleteCourseStatus(id);

    verify(repository, times(1)).deleteCourseStatus(id);
  }

  @Test
  void 受講生詳細の条件検索_正しく結果が返ること() {
    // Arrange
    Student student = new Student();
    student.setId(1);
    student.setName("田中");

    StudentCourse course1 = new StudentCourse();
    course1.setId(10);
    course1.setStudentId(1);
    course1.setCourseName("Javaコース");

    CourseStatus status1 = new CourseStatus();
    status1.setStudentCourseId(10);
    status1.setStatus("受講中");

    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setCourseName("Java");
    condition.setStatus("受講中");

    // モックの振る舞い設定
    when(repository.searchStudentDetailByCondition(condition)).thenReturn(List.of(student));
    when(repository.searchStudentCourse(1)).thenReturn(List.of(course1));
    when(repository.searchCourseStatusesByStudentId(1)).thenReturn(List.of(status1));

    // Act
    List<StudentDetail> result = sut.searchStudentDetailsByCondition(condition);

    // Assert
    Assertions.assertEquals(1, result.size());
    StudentDetail detail = result.get(0);
    Assertions.assertEquals(1, detail.getStudent().getId());
    Assertions.assertEquals(1, detail.getStudentCourseList().size());
    Assertions.assertEquals("受講中", detail.getStudentCourseList().get(0).getCourseStatus().getStatus());

    // Verify repository interaction
    verify(repository, times(1)).searchStudentDetailByCondition(condition);
    verify(repository, times(1)).searchStudentCourse(1);
    verify(repository, times(1)).searchCourseStatusesByStudentId(1);
  }
}