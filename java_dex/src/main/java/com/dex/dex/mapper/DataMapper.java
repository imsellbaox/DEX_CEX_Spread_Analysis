package com.dex.dex.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dex.dex.pojo.DexData;
import com.dex.dex.pojo.Sub;
import com.dex.dex.pojo.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DataMapper extends BaseMapper<DexData> {
    @Insert("INSERT INTO dex.datas VALUES(#{ts},#{datatime},#{adex},#{symbol},#{price},#{depth},#{dosub});")
    boolean insertone(DexData one);

    @Select("select t.* from dex.datas t;")
    List<DexData> findall();

    @Select("select t.* from dex.datas t where t.adex=#{adex} AND t.datatime >= #{T} AND t.dosub=false order by ts desc;")
    List<DexData> findbydex(String adex,long T);


}
