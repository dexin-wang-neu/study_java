package cn.neu.admin.mapper;

import cn.neu.admin.bean.City;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CityMapper {

//    @Insert("INSERT INTO city (name, state, country) VALUES(#{name}, #{state}, #{country})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(City city);     //这里使用注解版本插入数据

    @Select("SELECT id, name, state, country FROM city WHERE id = #{id}")
    City findById(long id);

}