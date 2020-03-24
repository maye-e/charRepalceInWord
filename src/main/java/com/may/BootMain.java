package com.may;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.may.config.PropVO;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

@SpringBootApplication
public class BootMain {

    static final Log log = LogFactory.get("提示");
    static int index = 0;//处理索引. 多线程下应用 AtomicInteger

    public static void main(String[] args) {
        //启动spring容器,返回一个ApplicationContext,亦可以用其getBean方法获取组件,此方式简单, 本文采用另一种方式获取组件
        SpringApplication.run(BootMain.class,args);

       /* if (MapUtil.isEmpty(PropVO.getReplaces())){
            log.warn("请配置需替换的字符!");
            return;
        }*/


        // 文件过滤器,只要目标文件
        FileFilter fileFilter = file -> {
            if (FileUtil.pathEndsWith(file, "docx") || FileUtil.pathEndsWith(file, "doc")) {
                return true;
            }
            return false;
        };

        List<File> files = FileUtil.loopFiles(PropVO.getDirectory(), fileFilter);
        log.info("识别到 {} 个待替换文档",files.size());

        log.info("开始处理文档,计时开启!");
        TimeInterval interval = new TimeInterval();
        interval.start();
        if (!PropVO.getShowDetail()){
            log.info("正在处理中...");
        }
        files.stream().forEach(file -> {
            //加载Word文档
            Document document = new Document(file.getPath());
            PropVO.getReplaces().forEach((original, update) -> {
                //使用新文本替换文档中的指定文本
                document.replace(original, update, PropVO.getCaseSensitive(), true);
            });
            //保存文档
            document.saveToFile(file.getPath(), FileFormat.Docx_2013);
            if (PropVO.getShowDetail()){
                log.info("处理完成: {} ,第 {}/{} 个",file.getPath(),++index,files.size());
            }
        });
        Console.log("全部处理完成,共 {} 个文档, 耗时：{}", files.size(), interval.intervalPretty());
    }
}
