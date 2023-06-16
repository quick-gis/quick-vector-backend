package com.github.quickvectorbackend;

import jakarta.persistence.Entity;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@MapperScans(value = {
    @MapperScan("com.github.quickvectorbackend.gaode.subway.mapper")
})
@EntityScan(value = {
    "com.github.quickvectorbackend.gaode.subway.entity"
})
public class QuickVectorBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuickVectorBackendApplication.class, args);
  }

}
