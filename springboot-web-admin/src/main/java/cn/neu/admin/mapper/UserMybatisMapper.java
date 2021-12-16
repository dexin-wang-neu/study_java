package cn.neu.admin.mapper;

import cn.neu.admin.bean.UserMybatis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 继承BaseMapper，就不用写基本的增删改查
 */
@Mapper
public interface UserMybatisMapper extends BaseMapper<UserMybatis> {

}
