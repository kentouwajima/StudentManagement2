package raisetech.StudentManagement2.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private int id;
  private int studentId;
  private String courseName;
  private LocalDateTime courseStartAt;
  private LocalDateTime courseEndAt;
}
