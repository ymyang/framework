package com.ymyang.service.file.impl;

import com.ymyang.config.AliyunConfig;
import com.ymyang.entity.file.OssFileEntity;
import com.ymyang.framework.beans.exception.BusinessException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.ymyang.dto.file.FileDto;
import com.ymyang.mapper.file.OssFileMapper;
import com.ymyang.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.FileURLMapper;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${app.fileDir}")
    private String fileDir;

    @Autowired
    private AliyunConfig config;

    @Autowired
    private OssFileMapper fileMapper;

    @Override
    public FileDto upload(MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            String filePath = this.randomFilePath(file.getOriginalFilename());

            OSS oss = new OSSClientBuilder().build(config.getOss().getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
            PutObjectResult result = oss.putObject(config.getOss().getBucketName(), filePath, file.getInputStream());
            oss.shutdown();

            String fileUrl = config.getOss().getUrl() + "/" + filePath;
            FileDto fileDto = new FileDto();
            fileDto.setName(file.getOriginalFilename());
            fileDto.setUrl(fileUrl);

            this.saveFile(fileDto);

            return fileDto;
        } catch (Exception ex) {
            log.error("upload:" + file.getOriginalFilename(), ex);
            throw new BusinessException("文件上传失败：" + file.getOriginalFilename());
        }
    }

    @Override
    public FileDto upload(File file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            String filePath = this.randomFilePath(file.getName());

            OSS oss = new OSSClientBuilder().build(config.getOss().getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
            PutObjectResult result = oss.putObject(config.getOss().getBucketName(), filePath, file);
            oss.shutdown();

            String fileUrl = config.getOss().getUrl() + "/" + filePath;
            FileDto fileDto = new FileDto();
            fileDto.setName(file.getName());
            fileDto.setUrl(fileUrl);

            this.saveFile(fileDto);

            return fileDto;
        } catch (Exception ex) {
            log.error("upload:" + file.getName(), ex);
            throw new BusinessException("文件上传失败：" + file.getName());
        }
    }

    @Override
    public File downloadImg(String fileUrl) {
        try {
            String filePath = fileDir + "/" + config.getOss().getFileHost() + "/" + DateFormatUtils.format(new Date(), "yyyyMM");
            File fileDir = new File(filePath);
            fileDir.mkdirs();

            String fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            File file = new File(filePath, fileName);

            URL url = new URL(fileUrl);
            FileUtils.copyInputStreamToFile(url.openStream(), file);

            return file;
        } catch (Exception ex) {
            log.error("download:" + fileUrl, ex);
        }
        return null;
    }

    private String randomFilePath(String fileName) {
        String path = config.getOss().getFileHost() + "/" + DateFormatUtils.format(new Date(), "yyyyMM") + "/";
        path += UUID.randomUUID().toString().replace("-", "");

        String ext = "";
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            ext = fileName.substring(index);
        }
        return path + ext;
    }

    @Transactional
    private void saveFile(FileDto fileDto) {
        OssFileEntity file = new OssFileEntity();
        file.setName(fileDto.getName());
        file.setUrl(fileDto.getUrl());
        if (file.getName() == null) {
            file.setName("");
        }
        if (file.getName().length() > 255) {
            file.setName(file.getName().substring(0, 255));
        }
        fileMapper.insert(file);
    }
}
