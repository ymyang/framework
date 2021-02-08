package com.ymyang.controller.common;

import com.ymyang.framework.beans.ResponseEntity;
import com.ymyang.framework.web.annotations.Anonymous;
import com.ymyang.dto.file.FileDto;
import com.ymyang.service.file.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("UploadController")
@RequestMapping("/api/file")
@Api(value = "FileUploadController", tags = {"文件上传"})
public class FileUploadController {

    @Autowired
    private FileService fileService;

    @Anonymous
    @ApiOperation(value = "单文件上传", notes = "作者：yangym02")
    @PostMapping("/upload")
    public ResponseEntity<FileDto> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        FileDto fileDto = fileService.upload(file);
        if (fileDto == null) {
            return ResponseEntity.error("上传失败");
        }
        return ResponseEntity.success(fileDto, "上传成功");
    }
}
