package raisetech.StudentManagement2.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement2.controller.converter.StudentConverter;
import raisetech.StudentManagement2.data.CourseStatus;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentCourse;
import raisetech.StudentManagement2.data.StudentSearchCondition;
import raisetech.StudentManagement2.domain.StudentDetail;
import raisetech.StudentManagement2.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList(){
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<CourseStatus> courseStatusList = repository.searchCourseStatuses(); // ステータス情報も取得

    // CourseStatusをStudentCourseにネストするためのMapを作成
    Map<Integer, CourseStatus> courseStatusMap = courseStatusList.stream()
        .collect(Collectors.toMap(CourseStatus::getStudentCourseId, status -> status));

    // studentCourseListに対して、対応するCourseStatusを設定
    studentCourseList.forEach(studentCourse -> {
      CourseStatus status = courseStatusMap.get(studentCourse.getId());
      if (status != null) {
        studentCourse.setCourseStatus(status);
      }
    });

    // converterを使ってStudentDetailに変換
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id　受講生ID
   * @return　受講生詳細
   */
  public StudentDetail searchStudent(String id){
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourseList = repository.searchStudentCourse(student.getId());
    List<CourseStatus> courseStatus = repository.searchCourseStatusesByStudentId(student.getId());

    Map<Integer, CourseStatus> courseStatusMap = courseStatus.stream()
        .collect(Collectors.toMap(CourseStatus::getStudentCourseId, cs -> cs));

    studentCourseList.forEach(sc -> sc.setCourseStatus(courseStatusMap.get(sc.getId())));

    return new StudentDetail(student, studentCourseList);
  }

  /**
   * 受講生詳細の登録を行います。受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース終了日を設定します。
   *
   * @param studentDetail　受講生詳細
   * @return　登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail){
    Student student = studentDetail.getStudent();

    repository.registerStudent(student);
    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      initStudentsCourse(studentCourse, student);
      repository.registerStudentCourse(studentCourse);

      CourseStatus courseStatus = studentCourse.getCourseStatus();
      if (courseStatus != null) {
        courseStatus.setStudentCourseId(studentCourse.getId());
        repository.registerCourseStatus(courseStatus);
      }
    });
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourse　受講生コース情報
   * @param student　受講生
   */
  private void initStudentsCourse(StudentCourse studentCourse, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseStartAt(now);
    studentCourse.setCourseEndAt(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail　受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourseList()) {
      repository.updateStudentCourse(studentCourse);

      CourseStatus status = studentCourse.getCourseStatus();
      if (status != null) {
        repository.updateCourseStatus(status);
      }
    }
  }

  /**
   * コースステータスの一覧を取得します。
   *
   * @return courseStatusのリスト
   */
  public List<CourseStatus> searchCourseStatuses() {
    return repository.searchCourseStatuses();
  }

  /**
   * 単体のコースステータスを登録します。
   *
   * @param courseStatus 登録するステータス
   */
  public void registerCourseStatus(CourseStatus courseStatus) {
    StudentCourse studentCourse = repository.searchStudentCourseById(courseStatus.getStudentCourseId());
    if (studentCourse == null) {
      throw new IllegalArgumentException("指定されたコース情報が存在しません。");
    }
    repository.registerCourseStatus(courseStatus);
  }

  /**
   * 単体のコースステータスを更新します。
   *
   * @param courseStatus 更新するステータス
   */
  public void updateCourseStatus(CourseStatus courseStatus) {
    repository.updateCourseStatus(courseStatus);
  }

  /**
   * 単体のコースステータスを削除します。
   *
   * @param id 削除するCourseStatusのID
   */
  public void deleteCourseStatus(int id) {
    repository.deleteCourseStatus(id);
  }

  /**
   * 検索条件に基づいて受講生詳細を検索します。
   *
   * @param condition 検索条件（名前、エリア、年齢範囲、コース名、ステータスなど）
   * @return 検索条件に合致する受講生のリスト
   */
  public List<StudentDetail> searchStudentDetailsByCondition(StudentSearchCondition condition) {
    List<Student> students = repository.searchStudentDetailByCondition(condition);
    List<StudentDetail> result = new ArrayList<>();

    for (Student student : students) {
      List<StudentCourse> courses = repository.searchStudentCourse(student.getId());
      List<CourseStatus> statuses = repository.searchCourseStatusesByStudentId(student.getId());

      Map<Integer, CourseStatus> statusMap = statuses.stream()
          .collect(Collectors.toMap(CourseStatus::getStudentCourseId, cs -> cs));

      if (condition.getCourseName() != null && !condition.getCourseName().isEmpty()) {
        courses = courses.stream()
            .filter(course -> course.getCourseName().contains(condition.getCourseName()))
            .collect(Collectors.toList());
      }

      for (StudentCourse course : courses) {
        course.setCourseStatus(statusMap.get(course.getId()));
      }

      result.add(new StudentDetail(student, courses));
    }

    return result;
  }
}
