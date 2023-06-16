package com.github.quickvectorbackend.gaode.subway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "gaode_subway_line")
@TableName("gaode_subway_line")
public class GaoDeSubwayLineEntity {

  @Id
  @TableId
  private Long id;
  /**
   * {@link GaoDeSubwayEntity#gId}
   */
  private String gid;
  private String ln;
  private String su;
  private String kn;
  private String lo;
  private String ls;
  private String cl;
  private String la;
  private Integer x;
  private String li;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    GaoDeSubwayLineEntity that = (GaoDeSubwayLineEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
