package com.github.quickvectorbackend.ctr;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import com.github.quickvectorbackend.servlet.req.db.GeoJsonToShpReq;
import com.github.quickvectorbackend.utils.Geotools;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tools")
@Controller
public class GeoJsonToShpCtr {


  static Geotools geotools = new Geotools();
private static final Logger logger = LoggerFactory.getLogger(GeoJsonToShpCtr.class);

  /**
   * 下载ZIP压缩包(会对下载后的压缩包进行删除)
   *
   * @param file     zip压缩包文件
   * @param response 响应
   */
  public static void downloadZip(File file, HttpServletResponse response) {
    OutputStream toClient = null;
    try {
      // 以流的形式下载文件。
      BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
      byte[] buffer = new byte[fis.available()];
      fis.read(buffer);
      fis.close();
      // 清空response
      response.reset();
      toClient = new BufferedOutputStream(response.getOutputStream());

      response.addHeader("Access-Control-Allow-Credentials", "true");
      response.addHeader("Access-Control-Allow-Origin", "*");
      response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
      response.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");
      response.setCharacterEncoding("UTF-8");
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
      toClient.write(buffer);
      toClient.flush();
    } catch (Exception e) {
      logger.error("下载zip压缩包过程发生异常:", e);
    } finally {
      if (toClient != null) {
        try {
          toClient.close();
        } catch (IOException e) {
          logger.error("zip包下载关流失败:", e);
        }
      }
      //删除改临时zip包(此zip包任何时候都不需要保留,因为源文件随时可以再次进行压缩生成zip包)
      file.delete();
    }
  }

  @PostMapping("/geojson_to_shp")
  public void geoJsonToShp(
      @RequestBody GeoJsonToShpReq geoJsonToShpReq,
      HttpServletResponse response
  ) throws IOException {

    File tempFile = new File("gen-shp"+File.separator + UUID.randomUUID());
    tempFile.mkdir();
    FileUtil.writeString(geoJsonToShpReq.getJson(), tempFile.getAbsolutePath()+"a.geojson", "UTF-8");
    geotools.geojson2shp(tempFile.getAbsolutePath()+"a.geojson",
        tempFile.getAbsoluteFile().getAbsolutePath() + "/export.shp");

    File zip = ZipUtil.zip(tempFile);

    downloadZip(zip, response);
    tempFile.delete();
  }

}
