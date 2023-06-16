package com.github.quickvectorbackend.gaode.subway.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.quickvectorbackend.gaode.subway.GaoDeSubwayService;
import com.github.quickvectorbackend.gaode.subway.GaoDeSubwayUtils;
import com.github.quickvectorbackend.gaode.subway.entity.GaoDeSubwayEntity;
import com.github.quickvectorbackend.gaode.subway.entity.GaoDeSubwayLineEntity;
import com.github.quickvectorbackend.gaode.subway.entity.GaoDeSubwaySiteEntity;
import com.github.quickvectorbackend.gaode.subway.entity.GaoDeSubwaySiteTimeEntity;
import com.github.quickvectorbackend.gaode.subway.json.line.GA;
import com.github.quickvectorbackend.gaode.subway.json.line.LDTO;
import com.github.quickvectorbackend.gaode.subway.json.line.StDTO;
import com.github.quickvectorbackend.gaode.subway.json.time.Bo;
import com.github.quickvectorbackend.gaode.subway.json.time.DDTO;
import com.github.quickvectorbackend.gaode.subway.mapper.GaoDeSubwayLineMapper;
import com.github.quickvectorbackend.gaode.subway.mapper.GaoDeSubwayMapper;
import com.github.quickvectorbackend.gaode.subway.mapper.GaoDeSubwaySiteMapper;
import com.github.quickvectorbackend.gaode.subway.mapper.GaoDeSubwaySiteTimeMapper;
import com.github.quickvectorbackend.utils.Gps;
import com.github.quickvectorbackend.utils.PositionUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.Line;
import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GaoDeSubwayServiceImpl implements GaoDeSubwayService {

  private static final Logger logger = LoggerFactory.getLogger(GaoDeSubwayServiceImpl.class);
  @Autowired
  GaoDeSubwayLineMapper gaoDeSubwayLineMapper;
  @Autowired
  private GaoDeSubwayMapper mapper;
  @Autowired
  private GaoDeSubwaySiteMapper gaoDeSubwaySiteMapper;
  @Autowired
  private GaoDeSubwaySiteTimeMapper gaoDeSubwaySiteTimeMapper;

  public static void main(String[] args) {
    List<Integer> l = new ArrayList<>();
    l.add(1);
    l.add(2);
    l.add(3);
    l.add(4);

    for (int i = 0; i < l.size() - 1; i++) {
      int current = l.get(i);
      int next = l.get(i + 1);
      System.out.println(current + String.valueOf(next));
    }
  }

  @Override
  public void saveCity() {
    List<GaoDeSubwayEntity> gaoDeSubwayEntity = GaoDeSubwayUtils.getGaoDeSubwayEntity();
    for (GaoDeSubwayEntity deSubwayEntity : gaoDeSubwayEntity) {
      QueryWrapper<GaoDeSubwayEntity> queryWrapper = new QueryWrapper<>();
      queryWrapper.lambda().eq(GaoDeSubwayEntity::getGId, deSubwayEntity.getGId());
      GaoDeSubwayEntity gaoDeSubwayEntity1 = mapper.selectOne(queryWrapper);
      if (gaoDeSubwayEntity1 == null) {
        mapper.insert(deSubwayEntity);
      }
    }
  }

  @Override
  public void saveLines() throws JsonProcessingException {
    List<GaoDeSubwayEntity> gaoDeSubwayEntities = mapper.selectList(new QueryWrapper<>());
    for (GaoDeSubwayEntity gaoDeSubwayEntity : gaoDeSubwayEntities) {
      saveLine(gaoDeSubwayEntity.getGId(), gaoDeSubwayEntity.getPyName());

    }
  }

  @Override
  public Map<String, List<Geometry>> findLineByGid(String gid) {

    Map<String, List<Geometry>> res = new HashMap<>();
    QueryWrapper<GaoDeSubwayLineEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
        .eq(GaoDeSubwayLineEntity::getGid, gid);
    List<GaoDeSubwayLineEntity> gaoDeSubwayLineEntities = this.gaoDeSubwayLineMapper.selectList(
        queryWrapper);
    for (GaoDeSubwayLineEntity gaoDeSubwayLineEntity : gaoDeSubwayLineEntities) {
      List<Geometry> oneLine = this.findOneLine(gid, gaoDeSubwayLineEntity.getLs());
      res.put(gaoDeSubwayLineEntity.getLs(), oneLine);
    }
    return res;
  }

  @Override
  public void saveLinesTime() throws JsonProcessingException {
    List<GaoDeSubwayEntity> gaoDeSubwayEntities = mapper.selectList(new QueryWrapper<>());
    for (GaoDeSubwayEntity gaoDeSubwayEntity : gaoDeSubwayEntities) {
      saveLineTime(gaoDeSubwayEntity.getGId(), gaoDeSubwayEntity.getPyName());

    }
  }

  @Override
  public void saveLine(String gid, String name) throws JsonProcessingException {
    try {
      GA hh = GaoDeSubwayUtils.hh(gid, name);

      for (LDTO ldto : hh.getL()) {
        GaoDeSubwayLineEntity gaoDeSubwayLineEntity = new GaoDeSubwayLineEntity();
        gaoDeSubwayLineEntity.setGid(gid);
        gaoDeSubwayLineEntity.setLn(ldto.getLn());
        gaoDeSubwayLineEntity.setSu(ldto.getSu());
        gaoDeSubwayLineEntity.setKn(ldto.getKn());
        gaoDeSubwayLineEntity.setLo(ldto.getLo());
        gaoDeSubwayLineEntity.setLs(ldto.getLs());
        gaoDeSubwayLineEntity.setCl(ldto.getCl());
        gaoDeSubwayLineEntity.setLa(ldto.getLa());
        gaoDeSubwayLineEntity.setX(ldto.getX());
        gaoDeSubwayLineEntity.setLi(ldto.getLi());

        QueryWrapper<GaoDeSubwayLineEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(GaoDeSubwayLineEntity::getLs, gaoDeSubwayLineEntity.getLs())
            .eq(GaoDeSubwayLineEntity::getGid, gid)
        ;
        GaoDeSubwayLineEntity gaoDeSubwayLineEntity1 = gaoDeSubwayLineMapper.selectOne(
            queryWrapper);

        Long lineId = null;
        if (gaoDeSubwayLineEntity1 == null) {
          this.gaoDeSubwayLineMapper.insert(gaoDeSubwayLineEntity);
          lineId = gaoDeSubwayLineEntity.getId();
        } else {
          lineId = gaoDeSubwayLineEntity1.getId();

        }

        List<StDTO> st = ldto.getSt();

        for (StDTO stDTO : st) {
          GaoDeSubwaySiteEntity entity = new GaoDeSubwaySiteEntity();
          entity.setGid(gid);
          entity.setLineId(lineId);
          entity.setLs(ldto.getLs());
          entity.setRs(stDTO.getRs());
          entity.setUdpx(stDTO.getUdpx());
          entity.setSu(stDTO.getSu());
          entity.setUdsu(stDTO.getUdsu());
          entity.setN(stDTO.getN());
          entity.setSid(stDTO.getSid());
          entity.setP(stDTO.getP());
          entity.setR(stDTO.getR());
          entity.setUdsi(stDTO.getUdsi());
          entity.setT(stDTO.getT());
          entity.setSi(stDTO.getSi());
          entity.setSl(stDTO.getSl());
          entity.setUdli(stDTO.getUdli());
          entity.setPoiid(stDTO.getPoiid());
          entity.setLg(stDTO.getLg());
          entity.setSp(stDTO.getSp());

          if (!StringUtils.isEmpty(stDTO.getSl())) {
            String[] split = stDTO.getSl().split(",");

            if (split.length == 2) {

              entity.setGdx(new BigDecimal(split[0]));
              entity.setGdy(new BigDecimal(split[1]));

            }
          }

          QueryWrapper<GaoDeSubwaySiteEntity> queryWrapper1 = new QueryWrapper<>();
          queryWrapper1.lambda()
              .eq(GaoDeSubwaySiteEntity::getSid, entity.getSid())
              .eq(GaoDeSubwaySiteEntity::getGid, gid)
          ;
          GaoDeSubwaySiteEntity gaoDeSubwaySiteEntity = gaoDeSubwaySiteMapper.selectOne(
              queryWrapper1);

          if (gaoDeSubwaySiteEntity == null) {

            gaoDeSubwaySiteMapper.insert(entity);
          }
        }


      }


    } catch (Exception e) {
      e.printStackTrace();
      logger.error("地铁线保存异常 gid = [{}] name = [{}]", gid, name);
    }
  }

  @Override
  public void saveLineTime(String gid, String name) throws JsonProcessingException {
    try {
      Bo time = GaoDeSubwayUtils.time(gid, name);
      for (com.github.quickvectorbackend.gaode.subway.json.time.LDTO ldto : time.getL()) {
        List<com.github.quickvectorbackend.gaode.subway.json.time.StDTO> st = ldto.getSt();
        for (com.github.quickvectorbackend.gaode.subway.json.time.StDTO stDTO : st) {
          // 站点ID
          String siteId = stDTO.getSi();
          for (DDTO ddto : stDTO.getD()) {

            GaoDeSubwaySiteTimeEntity gaoDeSubwaySiteTimeEntity = new GaoDeSubwaySiteTimeEntity();
            gaoDeSubwaySiteTimeEntity.setSid(siteId);
            gaoDeSubwaySiteTimeEntity.setGid(gid);
            gaoDeSubwaySiteTimeEntity.setLs(ddto.getLs());
            gaoDeSubwaySiteTimeEntity.setLat(ddto.getLt());
            gaoDeSubwaySiteTimeEntity.setN(ddto.getN());
            gaoDeSubwaySiteTimeEntity.setFt(ddto.getFt());

            QueryWrapper<GaoDeSubwaySiteTimeEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                .eq(GaoDeSubwaySiteTimeEntity::getGid, gid)
                .eq(GaoDeSubwaySiteTimeEntity::getLs, gaoDeSubwaySiteTimeEntity.getLs())
                .eq(GaoDeSubwaySiteTimeEntity::getN, gaoDeSubwaySiteTimeEntity.getN());
            GaoDeSubwaySiteTimeEntity gaoDeSubwaySiteTimeEntity1 = gaoDeSubwaySiteTimeMapper.selectOne(
                queryWrapper);
            if (gaoDeSubwaySiteTimeEntity1 == null) {
              gaoDeSubwaySiteTimeMapper.insert(gaoDeSubwaySiteTimeEntity);
            }


          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      logger.error("地铁线时刻表保存异常 gid = [{}] name = [{}]", gid, name);

    }
  }

  @Override
  public List<Geometry> findOneLine(String gid, String ls) {
    QueryWrapper<GaoDeSubwaySiteEntity> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
        .eq(GaoDeSubwaySiteEntity::getGid, gid)
        .eq(GaoDeSubwaySiteEntity::getLs, ls);
    List<Geometry> res = new ArrayList<>();
    List<GaoDeSubwaySiteEntity> gaoDeSubwaySiteEntities = this.gaoDeSubwaySiteMapper.selectList(
        queryWrapper);

    for (int i = 0; i < gaoDeSubwaySiteEntities.size()-1; i++) {
      GaoDeSubwaySiteEntity start = gaoDeSubwaySiteEntities.get(i);
      GaoDeSubwaySiteEntity end = gaoDeSubwaySiteEntities.get(i + 1);

      try {
        Gps gps = PositionUtil.gcj02_To_Gps84(start.getGdx().doubleValue(),
            start.getGdy().doubleValue());
        Gps gps1 = PositionUtil.gcj02_To_Gps84(end.getGdx().doubleValue(),
            end.getGdy().doubleValue());
        // TODO: 2023/6/16 坐标系互通 
        Geometry read = reader.read(
            "LineString (" + start.getGdx() + " " + start.getGdy() + "," + end.getGdx() + " "
                + end.getGdy() + " )");
        res.add(read);
      } catch (ParseException e) {
        e.printStackTrace();
        logger.error("转换线异常");
      }
    }

    return res;
  }

  GeometryFactory geometryFactory = new GeometryFactory();

  WKTReader reader = new WKTReader(geometryFactory);
}
