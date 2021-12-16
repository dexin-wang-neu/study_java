package cn.neu.admin.service.impl;

import cn.neu.admin.bean.UserMybatis;
import cn.neu.admin.mapper.UserMybatisMapper;
import cn.neu.admin.service.UserMybatisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserMybatisServiceImpl extends ServiceImpl<UserMybatisMapper, UserMybatis> implements UserMybatisService {

}
