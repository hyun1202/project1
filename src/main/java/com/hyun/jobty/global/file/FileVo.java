package com.hyun.jobty.global.file;

/**
 * 서버에 파일 업로드 완료 후 파일 정보(저장 파일 경로, 파일 명 등) 저장
 * @param oriFileName 사용자가 업로드한 실제 파일 명
 * @param saveFileName 서버에 저장된 파일 명
 * @param saveFilePath 서버에 저장한 파일 경로
 * @param extension 파일 확장자
 */
public record FileVo(String oriFileName, String saveFileName, String saveFilePath, String extension) {
}
