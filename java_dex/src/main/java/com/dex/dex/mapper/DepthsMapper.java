package com.dex.dex.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dex.dex.pojo.Depths;
import com.dex.dex.pojo.DexData;
import com.dex.dex.pojo.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepthsMapper extends BaseMapper<Depths> {
    @Insert("INSERT INTO dex.depths VALUES(#{ts},#{datatime},#{dex},#{buy},#{sell});")
    boolean insertone(Depths one);
    @Select("select t.* from dex.depths t where t.dex=#{dex};")
    List<Depths> findallbydex(String dex);
}
