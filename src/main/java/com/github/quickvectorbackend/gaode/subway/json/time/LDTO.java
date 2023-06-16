package com.github.quickvectorbackend.gaode.subway.json.time;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LDTO {

  private String a;
  /**
   * 地铁线唯一标识
   */
  private String ls;
  private List<StDTO> st;
}
