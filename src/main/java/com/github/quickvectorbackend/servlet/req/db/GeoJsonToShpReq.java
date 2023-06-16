package com.github.quickvectorbackend.servlet.req.db;

import lombok.Data;

@Data
public class GeoJsonToShpReq {

  private String json;
  private String name;


}
