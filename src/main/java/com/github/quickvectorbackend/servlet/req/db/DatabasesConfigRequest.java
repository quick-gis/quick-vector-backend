package com.github.quickvectorbackend.servlet.req.db;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class DatabasesConfigRequest {

  private String host;
  private Integer port;
  private String username;
  private String password;

  private String db;
  private String field;
  private String table;
  private List<Map<String,String>> tableFields;
}
