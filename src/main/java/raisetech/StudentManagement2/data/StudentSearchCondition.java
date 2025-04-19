package raisetech.StudentManagement2.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSearchCondition {

  private String name;
  private String area;
  private Integer ageFrom;
  private Integer ageTo;
  private String courseName;
  private String status;
}
