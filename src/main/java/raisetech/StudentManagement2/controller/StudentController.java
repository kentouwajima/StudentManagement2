package raisetech.StudentManagement2.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement2.data.CourseStatus;
import raisetech.StudentManagement2.data.StudentSearchCondition;
import raisetech.StudentManagement2.domain.StudentDetail;
import raisetech.StudentManagement2.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生詳細一覧（全件）
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList(){
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。IDに紐づく任意の受講生の情報を取得します。
   * @param id　受講生ID
   * @return　受講生
   */
  @Operation(summary = "受講生詳細の取得", description = "指定されたIDに紐づく受講生の情報を取得します。")
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @NotBlank String id){
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail){
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。キャンセルフラグの更新もここで行います。（論理削除）
   *
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */
  @Operation(summary = "受講生情報の更新", description = "受講生の情報を更新します。論理削除（キャンセル）もここで対応します。")
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail){
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 登録されているすべてのコースステータス情報を取得します。
   *
   * @return コースステータスの一覧
   */
  @Operation(summary = "全ステータス取得", description = "全てのコースステータス情報を取得します。")
  @GetMapping("/courseStatusList")
  public List<CourseStatus> getAllCourseStatuses() {
    return service.searchCourseStatuses();
  }

  /**
   * 新しいコースステータスを登録します。
   *
   * @param courseStatus 登録するコースステータス情報
   * @return 登録完了メッセージ
   */
  @Operation(summary = "ステータス登録", description = "コースステータスを登録します。")
  @PostMapping("/registerCourseStatus")
  public ResponseEntity<String> registerCourseStatus(@RequestBody @Valid CourseStatus courseStatus) {
    try {
      service.registerCourseStatus(courseStatus);
      return ResponseEntity.ok("ステータスを登録しました。");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * 既存のコースステータス情報を更新します。
   *
   * @param courseStatus 更新対象のコースステータス情報（IDを含む必要があります）
   * @return 更新完了メッセージ
   */
  @Operation(summary = "ステータス更新", description = "コースステータスを更新します。")
  @PutMapping("/updateCourseStatus")
  public ResponseEntity<String> updateCourseStatus(@RequestBody @Valid CourseStatus courseStatus) {
    service.updateCourseStatus(courseStatus);
    return ResponseEntity.ok("ステータスを更新しました。");
  }

  /**
   * 指定されたIDに紐づくコースステータスを削除します。
   *
   * @param id 削除対象のコースステータスID
   * @return 削除完了メッセージ
   */
  @Operation(summary = "ステータス削除", description = "指定されたIDのコースステータスを削除します。")
  @DeleteMapping("/deleteCourseStatus/{id}")
  public ResponseEntity<String> deleteCourseStatus(@PathVariable int id) {
    service.deleteCourseStatus(id);
    return ResponseEntity.ok("ステータスを削除しました。");
  }

  /**
   * 検索条件に基づいた受講生詳細の検索を行います。
   *
   * @param condition 検索条件
   * @return 検索結果
   */
  @PostMapping("/searchStudentDetails")
  public List<StudentDetail> searchStudentDetails(@RequestBody StudentSearchCondition condition) {
    return service.searchStudentDetailsByCondition(condition);
  }
}
