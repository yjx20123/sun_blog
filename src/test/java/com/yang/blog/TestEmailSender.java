package com.yang.blog;

import com.yang.blog.utils.EmailSender;
import com.yang.blog.utils.TextUtils;

import javax.mail.MessagingException;

public class TestEmailSender {
    public static void main(String[] args) throws MessagingException {
        System.out.println(TextUtils.isEmailAddressOk("1835271170@qq.com"));

    }
}
