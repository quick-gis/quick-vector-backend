package com.github.quickvectorbackend.gaode.subway.json.time;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DDTO {

  /**
   * 地铁线
   */
  private String ls;
  /**
   * 某班车
   */
  private String lt;
  /**
   * 开往
   */
  private String n;
  /**
   * 首
   */
  private String ft;
  private String sid;

}
