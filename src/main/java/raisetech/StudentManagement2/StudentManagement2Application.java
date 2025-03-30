package raisetech.StudentManagement2;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagement2Application {

	private String name = "Enami Kouji";
	private String age = "37";

	public static void main(String[] args) {
		SpringApplication.run(StudentManagement2Application.class, args);
	}

	@GetMapping("/studentInfo")
	public String getStudentInfo(){
		return name + " " + age + "歳です";
	}

	@PostMapping("/studentInfo")
	public void setStudentInfo(String name, String age){
		this.name = name;
		this.age = age;
	}

	@PostMapping("/studentName")
	public void updateStudentName(String name){
		this.name = name;
	}

	@PostMapping("/studentAge")
	public void updateStudentAge(String age){
		this.age = age;
	}

}
