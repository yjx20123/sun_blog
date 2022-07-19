package com.yang.blog.service.impl;

import com.yang.blog.dao.ImageDao;
import com.yang.blog.pojo.BlogUser;
import com.yang.blog.pojo.Image;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.ImageService;
import com.yang.blog.utils.Constants;
import com.yang.blog.utils.IdWorker;
import com.yang.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Value("${blog.image.Path}")
    public String imagePath;
    @Value("${blog.image.max-size}")
    public long maxSize;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ImageDao imageDao;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");


    /**
     * 上传的路径：可以配置，在配置文件里配置
     * 上传的内容，命名-->可以用ID,-->每天一个文件夹保存
     * 限制文件大小，通过配置文件配置
     * 保存记录到数据库里
     * ID｜存储路径｜url｜原名称｜用户ID｜状态｜创建日期｜更新日期
     *
     * @param file
     * @return
     */

    /**
     * 上传的路径：可以配置，在配置文件里配置
     * 上传的内容，命名-->可以用ID,-->每天一个文件夹保存
     * 限制文件大小，通过配置文件配置
     * 保存记录到数据库里
     * ID｜存储路径｜url｜原名称｜用户ID｜状态｜创建日期｜更新日期
     *
     * @param file
     * @return
     */
    @Override
    public ResponseResult uploadImage(MultipartFile file) {
        //判断是否有文件
        if (file == null) {
            return ResponseResult.FAILED("图片不可以为空.");
        }
        //判断文件类型，我们只支持图片上传，比如说：png，jpg，gif
        String contentType = file.getContentType();
        log.info("contentType == > " + contentType);
        if (TextUtils.isEmmpty(contentType)) {
            return ResponseResult.FAILED("图片格式错误.");
        }
        //获取相关数据，比如说文件类型，文件名称
        String originalFilename = file.getOriginalFilename();
        log.info("originalFilename == > " + originalFilename);
        String type = getType(contentType, originalFilename);
        if (type == null) {
            return ResponseResult.FAILED("不支持此图片类型");
        }
        //限制文件的大小
        long size = file.getSize();
        log.info("maxSize === > " + maxSize + "  size ==> " + size);
        if (size > maxSize) {
            return ResponseResult.FAILED("图片最大仅支持" + (maxSize / 1024 / 1024) + "Mb");
        }
        //创建图片的保存目录
        //规则：配置目录/日期/类型/ID.类型
        long currentMillions = System.currentTimeMillis();
        String currentDay = simpleDateFormat.format(currentMillions);
        log.info("current day == > " + currentDay);
        String dayPath = imagePath + File.separator + currentDay;
        File dayPathFile = new File(dayPath);
        //判断日期文件夹是否存在//2020_06_26
        if (!dayPathFile.exists()) {
            dayPathFile.mkdirs();
        }
        String targetName = String.valueOf(idWorker.nextId());
        String targetPath = dayPath + File.separator + type + File.separator + targetName + "." + type;
        File targetFile = new File(targetPath);
        //判断类型文件夹是否存在//gif
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            log.info("targetFile == > " + targetFile);
            //保存文件
            file.transferTo(targetFile);
            //返回结果：包含这个图片的名称和访问路径
            //第一个是访问路径 -- > 得对应着解析来
            Map<String, String> result = new HashMap<>();
            String resultPath = currentMillions + "_" + targetName + "." + type;
            result.put("id", resultPath);
            //第二个是名称--->alt="图片描述",如果不写，前端可以使用名称作为这个描述
            result.put("name", originalFilename);
            Image image = new Image();
            image.setContentType(contentType);
            image.setId(targetName);
            image.setCreateTime(new Date());
            image.setUpdateTime(new Date());
            image.setPath(targetFile.getPath());
            image.setName(originalFilename);
            image.setUrl(resultPath);
            image.setState("1");
            BlogUser sobUser = userService.checkBlogUser();
            image.setUser_id(sobUser.getId());
            //记录文件
            //保存记录到数据里
            imageDao.save(image);
            //返回结果
            ResponseResult success = ResponseResult.SUCCESS("文件上传成功");
            success.setData(result);
            return success;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.FAILED("图片上传失败，请稍后重试");
    }


    private String getType(String contentType, String name) {
        String type = null;
        if (Constants.ImageType.TYPE_GIF_PREFIX.equals(contentType) && name.endsWith(Constants.ImageType.TYPE_GIF)) {
            type = Constants.ImageType.TYPE_GIF;
        } else if (Constants.ImageType.TYPE_JPG_PREFIX.equals(contentType) && name.endsWith(Constants.ImageType.TYPE_JPG)) {
            type = Constants.ImageType.TYPE_JPG;
        } else if (Constants.ImageType.TYPE_PNG_PREFIX.equals(contentType) && name.endsWith(Constants.ImageType.TYPE_PNG)) {
            type = Constants.ImageType.TYPE_PNG;
        } else if (Constants.ImageType.TYPE_JPEG_PREFIX.equals(contentType) && name.endsWith(Constants.ImageType.TYPE_JPEG)) {
            type = Constants.ImageType.TYPE_JPEG;
        }
        return type;
    }

    @Override
    public void getImage(HttpServletResponse response, String iamgeId) throws IOException {
        //配置的目录已知
        //根据尺寸
        //需要日期
        String[] paths = iamgeId.split("_");
        String pathValue = paths[0];
        String format = simpleDateFormat.format(Long.parseLong(pathValue));
        log.info("format==>" + format);
        //id
        String name = paths[0];
        //需要类型
        String type = name.substring(name.length() - 3);
        //使用日期的时间戳_ID、类型
        String targetPath = imagePath + File.separator + format + File.separator + type + File.separator + name;
        log.info("get image target path==>" + targetPath);
        File file = new File(targetPath);
        OutputStream writer = null;
        FileInputStream fos = null;
        try {
            response.setContentType("image/png");
            writer = response.getOutputStream();
            //读取
            fos = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = fos.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (writer != null) {


                writer.close();

            }
        }

    }

    @Override
    public ResponseResult listImages(int page, int size) {
        //处理page，size
        //创建分页
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = 1;
        }
        //限制size，每一页不得小于5个
        if (size < Constants.Page.MIN_SIZE) {
            size = Constants.Page.MIN_SIZE;
        }
        BlogUser blogUser = userService.checkBlogUser();
        if (blogUser == null) {
            return ResponseResult.FAILED("用户未登录");
        }
        final String userId = blogUser.getId();
        //创建分页条件
        new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Image> all = imageDao.findAll(new Specification<Image>() {
            @Override
            public Predicate toPredicate(Root<Image> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //根据用户ID
                Predicate userIdPre = criteriaBuilder.equal(root.get("user_id").as(String.class), userId);
                Predicate statePre = criteriaBuilder.equal(root.get("state").as(String.class), "1");
                return criteriaBuilder.and(userIdPre, statePre);
            }
        }, pageable);
        ResponseResult success = ResponseResult.SUCCESS("获取图片成功");
        success.setData(all);
        return success;
    }

    @Override
    public ResponseResult deleteImage(String imageId) {
        int result = imageDao.delteImageByUpdateState(imageId);
        return result > 0 ? ResponseResult.SUCCESS("删除成功") : ResponseResult.FAILED("删除失败");
    }
}
