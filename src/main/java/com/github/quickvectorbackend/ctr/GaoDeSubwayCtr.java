package com.github.quickvectorbackend.ctr;

import com.github.quickvectorbackend.gaode.subway.GaoDeSubwayService;
import com.github.quickvectorbackend.servlet.res.ResultResponse;
import java.util.List;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gaode")
public class GaoDeSubwayCtr {

  @Autowired
  private GaoDeSubwayService gaoDeSubwayService;
  @PostMapping("/findLineByGid/{gid}")
  public ResultResponse<Map<String, List<Geometry>>> findLineByGid(@PathVariable("gid") String gid) {

    return ResultResponse.ok(gaoDeSubwayService.findLineByGid(gid));
  }
}
