package com.may;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Map;

public class doMain {

    static final Setting setting = new Setting("conf/prop.setting");
    static final String path = setting.getStr("directory");//待处理文档目录
    static final Map<String, String> strMap = setting.getMap("replace_str");//待替换的字符集合
    static final Log log = LogFactory.get("提示");
    static int index = 0;//处理索引. 多线程下应用 AtomicInteger
    static boolean showDetail = false;//是否显示处理细节
    static boolean caseSensitive = true;//大小写是否敏感
    static {
        try {
            showDetail = setting.getBool("show_detail");
        }catch (Exception e){
            log.warn("conf/prop.setting 中的 show_detail 值已默认为false");
        }
        try {
            caseSensitive = setting.getBool("caseSensitive");
        }catch (Exception e){
            log.warn("conf/prop.setting 中的 caseSensitive 值已默认为true");
        }
    }

    public static void main(String[] args) {

        if (path == null){
            log.error("请配置 conf/prop.setting 中的 directory 值!!!");
            return;
        }else if (!FileUtil.isDirectory(path)){
            log.error("请配置正确的 directory 值!!!");
            return;
        }
        if (strMap != null && strMap.size() == 0){
            log.warn("请在 conf/prop.setting 中的 [replace_str] 下配置需替换的字符!");
            return;
        }

        // 文件过滤器,只要目标文件
        FileFilter fileFilter = file -> {
            if (FileUtil.pathEndsWith(file, "docx") || FileUtil.pathEndsWith(file, "doc")) {
                return true;
            }
            return false;
        };

        List<File> files = FileUtil.loopFiles(path, fileFilter);
        log.info("识别到 {} 个待替换文档",files.size());

        log.info("开始处理文档,计时开启!");
        TimeInterval interval = new TimeInterval();
        interval.start();
        if (!showDetail){
            log.info("正在处理中...");
        }
        files.stream().forEach(file -> {
            //加载Word文档
            Document document = new Document(file.getPath());
            strMap.forEach((original,update) -> {
                //使用新文本替换文档中的指定文本
                document.replace(original, update, caseSensitive, true);
            });
            //保存文档
            document.saveToFile(file.getPath(), FileFormat.Docx_2013);
            if (showDetail){
                log.info("处理完成: {} ,第 {}/{} 个",file.getPath(),++index,files.size());
            }
        });
        Console.log("全部处理完成,共 {} 个文档, 耗时：{}", files.size(), interval.intervalPretty());
    }
}
