package com.zhi.delivery.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhi.delivery.entity.AddressBook;
import com.zhi.delivery.mapper.AddressBookMapper;
import com.zhi.delivery.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
   implements AddressBookService {
}
