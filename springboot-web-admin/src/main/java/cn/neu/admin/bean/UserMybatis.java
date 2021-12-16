package cn.neu.admin.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * mybatis-plus CRUD操作的类
 * mybatis-plus 要求实体中的所有属性都在数据库中
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("user_mybatis")  //默认是UserMybatis驼峰形式在数据库中的表，也可以指定具体表
public class UserMybatis {

    /**
     * @TableFiled（exist = false)  表示属性在数据库中不存在
     */
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String password;

    //以下时是数据库中的字段
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
