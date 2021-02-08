package com.ymyang.service.file;

import com.ymyang.dto.file.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService {

    FileDto upload(MultipartFile file);

    FileDto upload(File file);

    File downloadImg(String fileUrl);
}
