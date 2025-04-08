package raisetech.StudentManagement2;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "受講生管理システム", description = "受講生とコース情報を管理するシステムのAPIドキュメント"))
@SpringBootApplication
public class StudentManagement2Application {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagement2Application.class, args);
	}
}
