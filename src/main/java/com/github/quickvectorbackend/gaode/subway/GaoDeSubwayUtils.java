package com.github.quickvectorbackend.gaode.subway;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.quickvectorbackend.gaode.subway.entity.GaoDeSubwayEntity;
import com.github.quickvectorbackend.gaode.subway.json.line.GA;
import com.github.quickvectorbackend.gaode.subway.json.time.Bo;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GaoDeSubwayUtils {

  static ObjectMapper objectMapper = new ObjectMapper();
  static Gson gson = new Gson();

  public static void main(String[] args) throws JsonProcessingException {
    String s = FileUtil.readString(
        new File("/Users/zhangsan/youkong/quick-vector-backend/src/main/resources/hanz.json"),
        "UTF-8");

    GA ga = objectMapper.readValue(s, GA.class);
    System.out.println();
  }

  public static GA hh(String gid, String cname) throws JsonProcessingException {
    String url =
        "http://map.amap.com/service/subway?_" + System.currentTimeMillis() + "&srhdata=" + gid
            + "_drw_" + cname
            + ".json";

    String s = HttpUtil.get(url);
    return objectMapper.readValue(s, GA.class);

  }

  public static Bo time(String gid, String cname) throws JsonProcessingException {
    String url =
        "http://map.amap.com/service/subway?_" + System.currentTimeMillis() + "&srhdata=" + gid
            + "_info_" + cname
            + ".json";

    String s = HttpUtil.get(url);
    return objectMapper.readValue(s, Bo.class);

  }

  public static List<GaoDeSubwayEntity> getGaoDeSubwayEntity() {
    String text = HttpUtil.get("http://map.amap.com/subway/index.html");

    String pattern = "<a.*?id=\"(.*?)\".*?cityname=\"(.*?)\">(.*?)</a>";
    Pattern regexPattern = Pattern.compile(pattern);
    Matcher matcher = regexPattern.matcher(text);

    List<GaoDeSubwayEntity> list = new ArrayList<>();
    while (matcher.find()) {
      String idData = matcher.group(1);
      String citynameData = matcher.group(2);
      String aTagName = matcher.group(3);
      GaoDeSubwayEntity e = new GaoDeSubwayEntity();
      e.setGId(idData);
      e.setPyName(citynameData);
      e.setCnName(aTagName);

      list.add(e);
    }
    return list;
  }

}
