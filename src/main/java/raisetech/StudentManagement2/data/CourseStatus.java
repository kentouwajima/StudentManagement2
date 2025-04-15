package raisetech.StudentManagement2.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "コース申し込み状況")
@Getter
@Setter
public class CourseStatus {

  private int id;
  private int studentCourseId;
  private String status;
}