package club.banyuan.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("club.banyuan.project.common.mapper")
@SpringBootApplication
public class ProjectStudyroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectStudyroomApplication.class, args);
    }

}
