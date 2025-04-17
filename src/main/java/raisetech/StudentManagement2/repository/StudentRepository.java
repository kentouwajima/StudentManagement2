package raisetech.StudentManagement2.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement2.data.CourseStatus;
import raisetech.StudentManagement2.data.Student;
import raisetech.StudentManagement2.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   *　受講生の全件検索を行います。
   *
   * @return　受講生一覧（全件）
   */
  List<Student> search();

  /**
   *　受講生の検索を行います。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  Student searchStudent(String id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return　受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId　受講生ID
   * @return　受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(int studentId);

  /**
   * 受講生IDに紐づくコースステータス情報をすべて取得します。
   *
   * @return コースステータスのリスト（全件）
   */
  List<CourseStatus> searchCourseStatuses();

  /**
   * 指定された受講生IDに紐づくコースステータス情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生に紐づくコースステータスのリスト
   */
  List<CourseStatus> searchCourseStatusesByStudentId(int studentId);

  /**
   * 受講生を新規登録します。IDに関しては自動採番を行う。
   *
   * @param student　受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。IDに関しては自動採番を行う。
   *
   * @param studentCourse　受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * コースステータス情報を新規登録します。IDは自動採番されます。
   *
   * @param courseStatus 登録するコースステータス情報
   */
  void registerCourseStatus(CourseStatus courseStatus);

  /**
   * 受講生を更新します。
   *
   * @param student　受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse　受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * コースステータス情報を更新します。
   *
   * @param courseStatus 更新対象のコースステータス情報
   */
  void updateCourseStatus(CourseStatus courseStatus);

  /**
   * course_status を ID で削除
   */
  void deleteCourseStatus(int id);

  /**
   * 受講生コース情報をIDで検索します。
   *
   * @param id 受講生コースID
   * @return 受講生コース情報（指定されたIDの情報）
   */
  StudentCourse searchStudentCourseById(int id);

}
