package cn.neu.admin;

import cn.neu.admin.bean.User;
import cn.neu.admin.bean.UserMybatis;
import cn.neu.admin.mapper.UserMybatisMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
class SpringbootWebAdminApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    UserMybatisMapper userMybatisMapper;


    @Test
    void contextLoads() {
//        jdbcTemplate.queryForObject("select * from user");
//        jdbcTemplate.queryForList("select * from user");
        Long count = jdbcTemplate.queryForObject("select count(*) from user", Long.class);
        log.info("记录总数：{}",count);
        log.info("数据源类型：{}",dataSource.getClass());
    }
    @Test
    void testUserMybatisMapper() {
        UserMybatis userMybatis = userMybatisMapper.selectById(1L);
        log.info("用户信息:{}",userMybatis);
    }
}
