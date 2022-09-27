package com.dex.dex.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dex.dex.pojo.DexData;
import com.dex.dex.pojo.Sub;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubDataMapper extends BaseMapper<Sub> {
    @Select("select t.* from dex.pricesub t where t.abdex=#{abdex};")
    List<Sub> findallsubs(String abdex);



    @Insert("INSERT INTO dex.pricesub VALUES(#{ts},#{symbol},#{abdex},#{pricesub});")
    void insertonesub(Sub sub);
}
