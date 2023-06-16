package com.github.quickvectorbackend.gaode.subway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
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
@Table(name = "gaode_subway_line_site_timr")
@TableName("gaode_subway_line_site_timr")
public class GaoDeSubwaySiteTimeEntity {
  @Id
  @TableId
  private Long id;

  /**
   * 地铁线
   */
  private String ls;
  /**
   * 某班车
   */
  private String lat;
  /**
   * 开往
   */
  private String n;
  /**
   * 首
   */
  private String ft;

  private String sid;
  private String gid;
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    GaoDeSubwaySiteTimeEntity that = (GaoDeSubwaySiteTimeEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
