package com.github.quickvectorbackend.gaode.subway.json.time;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
@JsonIgnoreProperties(ignoreUnknown = true)

@NoArgsConstructor
@Data
public class Bo {

  private String i;
  private List<LDTO> l;
}
