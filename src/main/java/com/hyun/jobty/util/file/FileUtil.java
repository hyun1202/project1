package com.hyun.jobty.util.file;

import com.hyun.jobty.conf.property.ApplicationContextProvider;
import com.hyun.jobty.conf.property.GlobalProperty;
import com.hyun.jobty.util.Util;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtil {
    private String basicPath;
    private static List<FileVo> fileInfo = new ArrayList<>();

    public FileUtil(){
        this.basicPath = ApplicationContextProvider.getBean("globalProperty.Jobty.File", GlobalProperty.Jobty.File.class).getUpload_path();
    }

    public static List<FileVo> getMultiFileInfo() {
        return fileInfo;
    }

    public static FileVo getSingleFileInfo() {
        return fileInfo.get(0);
    }

    public Boolean uploadFile(MultipartFile file, String pathId){
        return this.saveFile(List.of(file), pathId);
    }

    public Boolean uploadFiles(List<MultipartFile> file, String memberId, String pathId){
        String resultPathId = pathId;
        if (!memberId.equals("")){
            resultPathId = memberId + File.separator + pathId;
        }
        return this.saveFile(file, resultPathId);
    }

    private Boolean saveFile(List<MultipartFile> files, String pathId){
        fileInfo.clear();
        String filePath = basicPath + File.separator + pathId;
        try{
            File folder = new File(filePath);
            if (!folder.exists()){
                folder.mkdirs();
            }

            for (MultipartFile file : files){
                String oriFileName = Normalizer.normalize(Objects.requireNonNull(file.getOriginalFilename()), Normalizer.Form.NFC);
                String saveFileName = Util.random() + "." + Util.getFileExtension(oriFileName);
                String saveFilePath = filePath + File.separator + saveFileName;
                File dst = new File(saveFilePath);
                file.transferTo(dst);
                fileInfo.add(new FileVo(oriFileName, saveFileName, saveFilePath, Util.getFileExtension(oriFileName)));
            }
            return true;
        }catch (Exception e){
            for (int i=0; i<fileInfo.size(); i++){
                String saveFilePath = fileInfo.get(i).saveFilePath();
                new File(saveFilePath + "." + Util.getFileExtension(saveFilePath)).delete();
            }
        }
        return false;
    }

    public UrlResource downloadFile(FileVo file) throws MalformedURLException {
        return new UrlResource("file:" + file.saveFilePath());
    }
}
