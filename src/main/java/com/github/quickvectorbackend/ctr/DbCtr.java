package com.github.quickvectorbackend.ctr;

import com.github.quickvectorbackend.servlet.req.db.DatabasesConfigRequest;
import com.github.quickvectorbackend.servlet.res.ResultResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db")
public class DbCtr {

  public static final String JDBC_MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";


  /**
   * 获取数据库列表
   *
   * @param databasesConfigRequest
   * @return
   */
  @PostMapping("/dbs")
  public ResultResponse<List<String>> dbs(
      @RequestBody DatabasesConfigRequest databasesConfigRequest
  ) {

    JdbcTemplate jdbcTemplate = getJdbcTemplate(databasesConfigRequest);
    List<String> query = jdbcTemplate.query("show databases", new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("Database");
      }
    });
    return ResultResponse.ok(query);
  }

  /**
   * 获取数据库表集合
   *
   * @param databasesConfigRequest
   * @return
   */
  @PostMapping("/tables")
  public ResultResponse<List<Map<String,String>>> tables(
      @RequestBody DatabasesConfigRequest databasesConfigRequest
  ) {

    JdbcTemplate jdbcTemplate = getJdbcTemplate(databasesConfigRequest);
    List<Map<String,String>> query = jdbcTemplate.query(
        "SELECT table_name 表名,table_comment 表说明 FROM information_schema.TABLES  WHERE table_schema = '"
            + databasesConfigRequest.getDb() + "' ORDER BY table_name", new RowMapper<Map<String,String>>() {
          @Override
          public Map<String,String> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, String> res = new HashMap<>();
            res.put("cn_name", rs.getString("表名"));
            res.put("en_name", rs.getString("表说明"));
            return res;

          }
        });
    return ResultResponse.ok(query);
  }

  /**
   * 获取数据库表字段集合
   *
   * @param databasesConfigRequest
   * @return
   */
  @PostMapping("/tableFields")
  public ResultResponse<List<Map<String,String>>> tableFields(
      @RequestBody DatabasesConfigRequest databasesConfigRequest
  ) {

    JdbcTemplate jdbcTemplate = getJdbcTemplate(databasesConfigRequest);
    String formatted = """
               
        SELECT
        a.table_name 表名,
        a.table_comment 表说明,
        b.COLUMN_NAME 字段名,
        b.column_comment 字段说明,
        b.column_type 字段类型,
        b.column_key 约束
        FROM
        information_schema. TABLES a
        LEFT JOIN information_schema.COLUMNS b ON a.table_name = b.TABLE_NAME
        WHERE
        a.table_schema = '%s'
        and a.table_name = '%s'
        ORDER BY
        a.table_name;
        """.formatted(databasesConfigRequest.getDb(), databasesConfigRequest.getTable());
    List<Map<String, String>> query = jdbcTemplate.query(formatted,
        new RowMapper<Map<String, String>>() {
          @Override
          public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, String> res = new HashMap<>();
            res.put("cn_name", rs.getString("字段说明"));
            res.put("en_name", rs.getString("字段名"));
            res.put("type", rs.getString("字段类型"));
            return res;

          }
        });
    return ResultResponse.ok(query);
  }


  @PostMapping("/geojson")
  public ResultResponse<List<String>> geojson(
      @RequestBody DatabasesConfigRequest databasesConfigRequest

  ){
    JdbcTemplate jdbcTemplate = getJdbcTemplate(databasesConfigRequest);
    StringBuilder fied = new StringBuilder();
    for (Map<String,String> field : databasesConfigRequest.getTableFields()) {
      if (!field.get("en_name").equals(databasesConfigRequest.getField())) {

        fied.append("'").append(field.get("en_name")).append("' ,").append(field.get("en_name")).append(",\n");
      }
    }
    String fd = fied.substring(0, fied.length() - 2).toString();
    String sql = """
         SELECT JSON_OBJECT(
         'type', 'FeatureCollection',
          'features', JSON_ARRAYAGG(
           JSON_OBJECT(
            'type', 'Feature',
             'geometry', ST_AsGeoJSON(
             %s
            ),
              'properties', JSON_OBJECT(%s)
               
        ))) as geojson from %s ;""".formatted(databasesConfigRequest.getField(), fd,
        databasesConfigRequest.getTable());
    List<String> query = jdbcTemplate.query(sql, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("geojson");
      }

    });

    System.out.println();

    return ResultResponse.ok(query);
  }


  private JdbcTemplate getJdbcTemplate(DatabasesConfigRequest databasesConfigRequest) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(JDBC_MYSQL_DRIVER);
    dataSource.setUrl(
        "jdbc:mysql://" + databasesConfigRequest.getHost() + ":" + databasesConfigRequest.getPort()
            + "/");
    dataSource.setUsername(databasesConfigRequest.getUsername());
    dataSource.setPassword(databasesConfigRequest.getPassword());
    if (!org.apache.commons.lang3.StringUtils.isEmpty(databasesConfigRequest.getDb())) {
      dataSource.setSchema(databasesConfigRequest.getDb());
      dataSource.setCatalog(databasesConfigRequest.getDb());
    }
    return new JdbcTemplate(dataSource);
  }


}
