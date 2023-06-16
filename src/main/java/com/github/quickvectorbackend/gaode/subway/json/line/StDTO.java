package com.github.quickvectorbackend.gaode.subway.json.line;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StDTO {

  private String rs;
  private String udpx;
  private String su;
  private String udsu;
  private String n;
  /**
   * 唯一
   */
  private String sid;
  private String p;
  private String r;
  private String udsi;
  private String t;
  private String si;
  private String sl;
  private String udli;
  private String poiid;
  private String lg;
  private String sp;
}
