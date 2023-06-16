package com.github.quickvectorbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.quickvectorbackend.gaode.subway.GaoDeSubwayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuickVectorBackendApplicationTests {


  @Autowired
  private GaoDeSubwayService gaoDeSubwayService;
  @Test
  void contextLoads() throws JsonProcessingException {
    gaoDeSubwayService.saveLinesTime();
  }


  @Test
  public void lines(){
    gaoDeSubwayService.findOneLine("3301","330100023133");
  }
}
