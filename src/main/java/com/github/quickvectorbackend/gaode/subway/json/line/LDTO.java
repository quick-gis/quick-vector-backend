package com.github.quickvectorbackend.gaode.subway.json.line;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LDTO {

  /**
   * 地铁名称
   */
  private String ln;
  private String su;
  private String kn;
  private String lo;
  /**
   * 唯一标识
   */
  private String ls;
  private String cl;
  private String la;
  private Integer x;
  private String li;
  private List<StDTO> st;
  private List<String> c;
  private List<String> lp;
  private List<FDTO> f;
}
