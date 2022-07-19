package com.yang.blog.utils;

public interface Constants {
    int DEFAULT_SIZE = 30;

    interface TimeValue {
        int HOUR_2 = 60 * 60 * 2;
        int MIN = 60;
        int HOUR = 60 * MIN;
        int Day = 24 * HOUR;
        int WEEK = 7 * Day;
        int MONTH = Day * 30;
    }

    interface User {
        String ROLE_ADMIN = "role_admin";
        String ROLE_NORMAL = "role_normal";
        String DEFAULT_AVATAR = "https://img1.baidu.com/it/u=1925715390,133119052&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400";
        String DEFAULT_STATE = "1";
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        String KEY_EMAIL_SEND_IP = "key_email_send_ip";
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address";
        String TOKEN_KEY = "token_key";
        String COOKIE_TOKEN_KEY = "cookie_token_key";
    }

    interface Settings {
        String MANAGER_ACCOUNT_INIT_STATE = "manager_account_init_state";
        String WEB_SIZE_TITLE = "web_size_title";
        String WEB_SIZE_KEYWORDS = "web_size_keywords";
        String WEB_SIZE_DESCRIPTION = "web_size_description";
        String WEB_SIZE_VIEW_COUNT = "web_size_view_count";
    }

    interface Page {
        int DEFAULT_PAGE = 1;
        int MIN_SIZE = 2;
    }

    interface ImageType {
        String PREFIX = "image/";
        String TYPE_JPG = "jpg";
        String TYPE_JPEG = "jpeg";
        String TYPE_PNG = "png";
        String TYPE_GIF = "gif";
        String TYPE_JPG_PREFIX = PREFIX + TYPE_JPG;
        String TYPE_PNG_PREFIX = PREFIX + TYPE_PNG;
        String TYPE_GIF_PREFIX = PREFIX + TYPE_GIF;
        String TYPE_JPEG_PREFIX = PREFIX + TYPE_JPEG;
    }

    interface Article {
        int SUMMARY_MAX_LENGTH = 256;
        int TITLE_MAX_LENGTH = 128;
        String STATE_DELETE = "0";
        String STATE_PUBLISH = "1";
        String STATE_DRAFT = "2";
        String STATE_TOP = "3";
    }


}
