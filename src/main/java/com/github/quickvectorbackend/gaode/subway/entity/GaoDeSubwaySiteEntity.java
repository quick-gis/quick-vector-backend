package com.github.quickvectorbackend.gaode.subway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "gaode_subway_line_site")
@TableName("gaode_subway_line_site")
public class GaoDeSubwaySiteEntity {
  @Id
  @TableId
  private Long id;
  /**
   * {@link GaoDeSubwayEntity#gId}
   */
  private String gid;
  /**
   * {@link GaoDeSubwayLineEntity#id}
   */
  private Long lineId;
  private String ls;
  private String rs;
  private String udpx;
  private String su;
  private String udsu;
  private String n;
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
  @Column(precision = 16, scale = 9)
  private BigDecimal gdx;
  @Column(precision = 16, scale = 9)
  private BigDecimal gdy;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    GaoDeSubwaySiteEntity that = (GaoDeSubwaySiteEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
