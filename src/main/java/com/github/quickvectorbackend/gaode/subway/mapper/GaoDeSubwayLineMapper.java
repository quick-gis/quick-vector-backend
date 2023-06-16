package com.github.quickvectorbackend.gaode.subway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.quickvectorbackend.gaode.subway.entity.GaoDeSubwayEntity;
import com.github.quickvectorbackend.gaode.subway.entity.GaoDeSubwayLineEntity;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface GaoDeSubwayLineMapper extends BaseMapper<GaoDeSubwayLineEntity>{


}
