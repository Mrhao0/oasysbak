package cn.gson.oasys.mappers;

import cn.gson.oasys.model.entity.file.FileSplit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {
    void addfilesplit(@Param("fileSplit") FileSplit fileSplit);


    List<FileSplit> findsplitfile(@Param("type") Integer type, @Param("userid") Long userid);
}
