package com.github.quickvectorbackend.gaode.subway.json.line;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FDTO {

  private String li;
  private List<String> c;
}
