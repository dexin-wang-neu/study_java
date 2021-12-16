package cn.neu.admin.service;

import cn.neu.admin.bean.Account;
import cn.neu.admin.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountMapper accountMapper;

    public Account getAcctById(Long id){
        return accountMapper.getAcct(id);
    }
}
