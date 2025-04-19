package raisetech.StudentManagement2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement2.data.CourseStatus;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentCourse;
import raisetech.StudentManagement2.data.StudentSearchCondition;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること(){
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 受講生IDから検索できること() {
    Student student = sut.searchStudent("1");
    assertThat(student.getId()).isEqualTo(1);
    assertThat(student.getName()).isNotBlank();
  }

  @Test
  void 全ての受講生コース情報を取得できること() {
    List<StudentCourse> courseList = sut.searchStudentCourseList();
    assertThat(courseList).isNotEmpty();
  }

  @Test
  void 特定受講生のコース情報を取得できること() {
    List<StudentCourse> courses = sut.searchStudentCourse(1);
    assertThat(courses).isNotNull();
  }

  @Test
  void 全てのコースステータスを取得できること() {
    List<CourseStatus> courseStatuses = sut.searchCourseStatuses();
    assertThat(courseStatuses).isNotEmpty();
  }

  @Test
  void 指定した受講生のコースステータスを取得できること() {
    int studentId = 1;
    List<CourseStatus> courseStatuses = sut.searchCourseStatusesByStudentId(studentId);
    assertThat(courseStatuses).isNotEmpty();
  }

  @Test
  void 受講生の登録が行えること(){
    Student student = new Student();
    student.setName("江並公史");
    student.setKanaName("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmail("test@example.com");
    student.setArea("奈良県");
    student.setAge(36);
    student.setSex("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(11);
  }

  @Test
  void 受講生コース情報を登録できること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId(1);
    course.setCourseName("Javaコース");
    course.setCourseStartAt(LocalDateTime.of(2025, 4, 1, 0, 0));
    course.setCourseEndAt(LocalDateTime.of(2025, 6, 30, 0, 0));

    sut.registerStudentCourse(course);

    List<StudentCourse> actual = sut.searchStudentCourse(1);
    assertThat(actual.stream().anyMatch(c -> "Javaコース".equals(c.getCourseName()))).isTrue();
  }

  @Test
  void 受講生コースステータスを登録できること() {
    CourseStatus newCourseStatus = new CourseStatus();
    newCourseStatus.setStatus("仮申込");
    newCourseStatus.setStudentCourseId(1);

    sut.registerCourseStatus(newCourseStatus);

    List<CourseStatus> courseStatuses = sut.searchCourseStatusesByStudentId(1);
    assertThat(courseStatuses).isNotEmpty();
    assertThat(courseStatuses.get(0).getStatus()).isEqualTo("仮申込");
  }

  @Test
  void 受講生情報を更新できること() {
    Student student = sut.searchStudent("1");
    String oldNickname = student.getNickname();

    student.setNickname("更新済みニックネーム");
    sut.updateStudent(student);

    Student updated = sut.searchStudent("1");
    assertThat(updated.getNickname()).isEqualTo("更新済みニックネーム");
    assertThat(updated.getNickname()).isNotEqualTo(oldNickname);
  }

  @Test
  void 受講生コース名を更新できること() {
    List<StudentCourse> courses = sut.searchStudentCourse(1);
    StudentCourse course = courses.get(0);

    String oldCourseName = course.getCourseName();
    course.setCourseName("更新済みコース");

    sut.updateStudentCourse(course);

    List<StudentCourse> updatedCourses = sut.searchStudentCourse(1);
    assertThat(updatedCourses.get(0).getCourseName()).isEqualTo("更新済みコース");
    assertThat(updatedCourses.get(0).getCourseName()).isNotEqualTo(oldCourseName);
  }

  @Test
  void 受講生コースステータスを更新できること() {
    CourseStatus existingCourseStatus = new CourseStatus();
    existingCourseStatus.setId(1);
    existingCourseStatus.setStatus("仮申込");
    existingCourseStatus.setStudentCourseId(1);

    sut.updateCourseStatus(existingCourseStatus);

    List<CourseStatus> courseStatuses = sut.searchCourseStatusesByStudentId(1);
    assertThat(courseStatuses).isNotEmpty();
    assertThat(courseStatuses.get(0).getStatus()).isEqualTo("仮申込");
  }

  @Test
  void コースステータスを削除できること() {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setStatus("削除対象ステータス");
    courseStatus.setStudentCourseId(1);
    sut.registerCourseStatus(courseStatus);

    List<CourseStatus> allStatuses = sut.searchCourseStatusesByStudentId(1);
    CourseStatus target = allStatuses.get(allStatuses.size() - 1);
    int targetId = target.getId();

    sut.deleteCourseStatus(targetId);

    List<CourseStatus> afterDelete = sut.searchCourseStatusesByStudentId(1);
    boolean isDeleted = afterDelete.stream().noneMatch(cs -> cs.getId() == targetId);

    assertThat(isDeleted).isTrue();
  }

  @Test
  void 名前による受講生検索ができること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setName("山田");
    List<Student> actual = sut.searchStudentDetailByCondition(condition);
    assertThat(actual).isNotEmpty();
    assertThat(actual.get(0).getName()).contains("山田");
  }

  @Test
  void エリアによる受講生検索ができること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setArea("東京都");
    List<Student> actual = sut.searchStudentDetailByCondition(condition);
    assertThat(actual).isNotEmpty();
    assertThat(actual.get(0).getArea()).contains("東京都");
  }

  @Test
  void 年齢範囲による受講生検索ができること() {
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setAgeFrom(20);
    condition.setAgeTo(30);
    List<Student> actual = sut.searchStudentDetailByCondition(condition);
    assertThat(actual).isNotEmpty();
    actual.forEach(student -> {
      assertThat(student.getAge()).isBetween(20, 30);
    });
  }

  @Test
  void コース名で受講生を検索できること() {
    // コース名「Javaコース」に該当する受講生を検索
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setCourseName("Javaコース");

    // 検索結果を取得
    List<Student> actual = sut.searchStudentDetailByCondition(condition);

    // 受講生のコース名に「Javaコース」が含まれていることを確認
    assertThat(actual).allMatch(student ->
        sut.searchStudentCourse(student.getId()).stream()
            .anyMatch(course -> "Javaコース".equals(course.getCourseName()))
    );
  }
}