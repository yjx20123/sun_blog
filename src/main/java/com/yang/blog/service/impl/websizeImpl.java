package com.yang.blog.service.impl;

import com.yang.blog.dao.SettingDao;
import com.yang.blog.pojo.Setting;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.WebSizeInfoService;
import com.yang.blog.utils.Constants;
import com.yang.blog.utils.IdWorker;
import com.yang.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@Transactional
public class websizeImpl implements WebSizeInfoService {
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult getWebTitle() {
        Setting title = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_TITLE);
        ResponseResult success = ResponseResult.SUCCESS("获取网站title成功");
        success.setData(title);
        return success;
    }

    @Override
    public ResponseResult putWebSizeTitle(String title) {
        if (TextUtils.isEmmpty(title)) {
            return ResponseResult.FAILED("网站Title不可为空!!!");
        }
        Setting titleFromDb = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_TITLE);
        if (titleFromDb == null) {
            titleFromDb = new Setting();
            titleFromDb.setId(idWorker.nextId() + "");
            titleFromDb.setKey(Constants.Settings.WEB_SIZE_TITLE);
            titleFromDb.setCreateTime(new Date());
            titleFromDb.setUpdateTime(new Date());
        }
        titleFromDb.setValue(title);
        settingDao.save(titleFromDb);
        return ResponseResult.SUCCESS("网站Title更新成功");
    }

    @Override
    public ResponseResult putSeoInfo(String keywords, String description) {
        if (TextUtils.isEmmpty(keywords)) {
            return ResponseResult.FAILED("关键字不可为空!!!");
        }
        if (TextUtils.isEmmpty(description)) {
            return ResponseResult.FAILED("描述不可为空!!!");
        }
        Setting descrptionDB = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        Setting keywordDB = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        if (descrptionDB == null) {
            descrptionDB = new Setting();
            descrptionDB.setKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
            descrptionDB.setId(idWorker.nextId() + "");
            descrptionDB.setCreateTime(new Date());
            descrptionDB.setUpdateTime(new Date());
        }
        descrptionDB.setValue(description);
        if (keywordDB == null) {
            keywordDB = new Setting();
            keywordDB.setKey(Constants.Settings.WEB_SIZE_KEYWORDS);
            keywordDB.setId(idWorker.nextId() + "");
            keywordDB.setCreateTime(new Date());
            keywordDB.setUpdateTime(new Date());
        }
        keywordDB.setValue(keywords);
        settingDao.save(descrptionDB);
        settingDao.save(keywordDB);
        return ResponseResult.SUCCESS("网站SEO信息修改成功");
    }

    @Override
    public ResponseResult getSeoInfo() {
        Setting description = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        Setting keyWords = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        Map<String, String> result = new HashMap<>();
        if (description != null && keyWords != null) {
            result.put(description.getKey(), description.getValue());
            result.put(keyWords.getKey(), keyWords.getValue());
        }
        log.info("result"+result);
        ResponseResult success = ResponseResult.SUCCESS("获取SEO信息成功.");
        success.setData(result);
        return success;
    }

    @Override
    public ResponseResult getWebSizeViewCount() {
        Setting webSizeDB = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_VIEW_COUNT);
        if (webSizeDB == null) {
            webSizeDB = new Setting();
            webSizeDB.setId(idWorker.nextId() + "");
            webSizeDB.setKey(Constants.Settings.WEB_SIZE_VIEW_COUNT);
            webSizeDB.setCreateTime(new Date());
            webSizeDB.setUpdateTime(new Date());
            webSizeDB.setValue("1");
        }
        settingDao.save(webSizeDB);
        Map<String, Integer> result = new HashMap<>();
        result.put(webSizeDB.getKey(), Integer.valueOf(webSizeDB.getValue()));

        ResponseResult success = ResponseResult.SUCCESS("获取网站浏览量成功");
        success.setData(webSizeDB);
        return success;

    }
}
