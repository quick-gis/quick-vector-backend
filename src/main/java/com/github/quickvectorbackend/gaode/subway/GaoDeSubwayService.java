package com.github.quickvectorbackend.gaode.subway;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.Line;
import org.locationtech.jts.geom.Geometry;

public interface GaoDeSubwayService {


  void saveCity();

  void saveLine(String gid, String name) throws JsonProcessingException;
  void saveLineTime(String gid, String name) throws JsonProcessingException;
  void saveLinesTime() throws JsonProcessingException;
  void saveLines() throws JsonProcessingException;


  Map<String, List<Geometry>> findLineByGid(String gid);
  List<Geometry> findOneLine(String gid, String ls);
}
