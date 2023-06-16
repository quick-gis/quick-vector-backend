package com.github.quickvectorbackend.gaode.subway.json.time;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StDTO {

  private String ac;
  /**
   * 地铁站唯一标识
   */
  private String si;
  private List<DDTO> d;
}
