package com.dex.dex.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dex.dex.pojo.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper extends BaseMapper<Task> {
    @Insert("INSERT INTO dex.tasks VALUES(#{ts},#{A_dex},#{B_dex},#{depth_level},#{symbol},#{status},#{exsit});")
    boolean insertone(Task one);

    @Select("SELECT t.* FROM dex.tasks t where t.exsit=1;")
    List<Task> findall();

    @Select("SELECT t.* FROM dex.tasks t where t.ts=#{ts} AND t.exsit=1;")
    Task Getbyts(long ts);
}
