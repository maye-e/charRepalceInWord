package com.may.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PropVO {
    private static String directory;
    private static Boolean showDetail;
    private static Boolean caseSensitive;
    private static Map<String,String> replaces;

    public static String getDirectory() {
        return directory;
    }

    @Value("${directory}")
    public void setDirectory(String directory) {
        /*if (!FileUtil.isDirectory(PropVO.getDirectory())){
            throw new RuntimeException("请配置正确的文档路径!!!");
        }*/
        PropVO.directory = directory;

    }

    public static Boolean getShowDetail() {
        return showDetail;
    }

    @Value("${showDetail}")
    public void setShowDetail(Boolean showDetail) {
        PropVO.showDetail = showDetail;
    }

    public static Boolean getCaseSensitive() {
        return caseSensitive;
    }

    @Value("${caseSensitive}")
    public void setCaseSensitive(Boolean caseSensitive) {
        PropVO.caseSensitive = caseSensitive;
    }

    public static Map<String,String> getReplaces() {
        return replaces;
    }

    @Value("#{${replaces}}")
    public void setReplaces(Map<String,String> replaces) {
        PropVO.replaces = replaces;
    }
}
