package com.hyun.jobty.global.util;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@NoArgsConstructor
public class FileUtil {
    @Value("${upload.path}")
    private String path;
    private static List<FileVo> fileInfo = new ArrayList<>();

    public List<FileVo> getFileInfo() {
        return fileInfo;
    }

    public Boolean uploadFile(MultipartFile file, String pathId){
        return this.uploadFiles(List.of(file), pathId);
    }

    public Boolean uploadFiles(List<MultipartFile> file, String memberId, String pathId){
        String resultPathId = pathId;
        if (!memberId.equals("")){
            resultPathId = memberId + File.separator + pathId;
        }
        return this.uploadFiles(file, resultPathId);
    }

    public Boolean uploadFiles(List<MultipartFile> files, String pathId){
        fileInfo.clear();
        String filePath = path + File.separator + pathId;
        try{
            File folder = new File(filePath);
            if (!folder.exists()){
                folder.mkdirs();
            }

            for (MultipartFile file : files){
                String oriFileName = file.getOriginalFilename();
                String saveFileName = UUID.randomUUID().toString().substring(1,8) + System.currentTimeMillis() + getExtension(oriFileName);
                String saveFilePath = filePath + File.separator + saveFileName;
                File dst = new File(saveFilePath);
                file.transferTo(dst);
                fileInfo.add(new FileVo(oriFileName, saveFileName, saveFilePath, getExtension(oriFileName, false)));
            }
            return true;
        }catch (Exception e){
            for (int i=0; i<fileInfo.size(); i++){
                String oriFileName = files.get(i).getOriginalFilename();
                new File(filePath + File.separator + fileInfo.get(i).saveFileName() + getExtension(oriFileName)).delete();
            }
        }
        return false;
    }

    private String getExtension(String fileName){
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension == null)
            return "";
        return "." + extension;
    }

    private String getExtension(String fileName, boolean dotRequired){
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension == null)
            return "";
        return dotRequired? "." + extension : extension;
    }


}
