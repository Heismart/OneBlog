package com.zyd.blog.file;

import cn.hutool.core.io.FileUtil;
import com.zyd.blog.file.entity.VirtualFile;
import com.zyd.blog.file.exception.LocalApiException;
import com.zyd.blog.file.util.BlogFileUtil;
import com.zyd.blog.file.util.BlogStreamUtil;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.util.Date;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://docs.zhyd.me
 * @date 2019/2/11 15:06
 * @since 1.8
 */
public class LocalApiClient extends BaseApiClient {
    private static final String DEFAULT_PREFIX = "oneblog/";

    private String url;
    private String rootPath;
    private String pathPrefix;

    public LocalApiClient() {
        super("Nginx文件服务器");
    }

    public LocalApiClient init(String url, String rootPath, String uploadType) {
        this.url = url;
        this.rootPath = rootPath;

        this.pathPrefix = StringUtils.isEmpty(uploadType) ? DEFAULT_PREFIX : uploadType.endsWith(File.separator) ? uploadType : uploadType + File.separator;
        return this;
    }

    @Override
    public VirtualFile uploadImg(InputStream is, String imageUrl) {
        this.check();

        String key = BlogFileUtil.generateTempFileName(imageUrl);
        this.createNewFileName(key, this.pathPrefix);
        Date startTime = new Date();

        String realFilePath = this.rootPath + this.newFileName;
        BlogFileUtil.checkFilePath(realFilePath);
        try (InputStream uploadIs = BlogStreamUtil.clone(is);
             InputStream fileHashIs = BlogStreamUtil.clone(is);
             FileOutputStream fos = new FileOutputStream(realFilePath)) {
            FileCopyUtils.copy(uploadIs, fos);
            return new VirtualFile()
                    .setOriginalFileName(FileUtil.getName(key))
                    .setSuffix(this.suffix)
                    .setUploadStartTime(startTime)
                    .setUploadEndTime(new Date())
                    .setFilePath(this.newFileName)
                    .setFileHash(DigestUtils.md5DigestAsHex(fileHashIs))
                    .setFullFilePath(this.url + this.newFileName);

        } catch (Exception e) {
            throw new LocalApiException("[" + this.storageType + "]文件上传失败：" + e.getMessage() + imageUrl);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean removeFile(String key) {
        this.check();
        if (StringUtils.isEmpty(key)) {
            throw new LocalApiException("[" + this.storageType + "]删除文件失败：文件key为空");
        }

        File file = new File(this.rootPath + key);
        try {
            return Files.deleteIfExists(file.toPath());
        }catch (DirectoryNotEmptyException e){
            throw new LocalApiException("[" + this.storageType + "]删除文件夹失败：先清空文件夹下的文件及子文件夹[" + this.rootPath + key + "]");
        }catch (SecurityException e){
            throw new LocalApiException("[" + this.storageType + "]没有足够的权限删除文件：" + e.getMessage());
        }catch (IOException e){
            throw new LocalApiException("[" + this.storageType + "]删除文件失败：文件不存在[" + this.rootPath + key + "]");
        }
    }

    @Override
    public void check() {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(rootPath)) {
            throw new LocalApiException("[" + this.storageType + "]尚未配置Nginx文件服务器，文件上传功能暂时不可用！");
        }
    }
}
