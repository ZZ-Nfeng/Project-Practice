package com.zhi.delivery.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhi.delivery.entity.User;
import com.zhi.delivery.mapper.UserMapper;
import com.zhi.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


}


