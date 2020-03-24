package com.may;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.may.config.PropVO;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

@SpringBootApplication
@Slf4j
public class BootMain {

    static int index = 0;//处理索引. 多线程下应用 AtomicInteger

    public static void main(String[] args) {
        //启动spring容器,返回一个ApplicationContext
        ConfigurableApplicationContext context = SpringApplication.run(BootMain.class, args);
        PropVO propVO = (PropVO)context.getBean("propVO");

        // 文件过滤器,只要目标文件
        FileFilter fileFilter = file -> {
            if (FileUtil.pathEndsWith(file, "docx") || FileUtil.pathEndsWith(file, "doc")) {
                return true;
            }
            return false;
        };
        List<File> files = FileUtil.loopFiles(propVO.getDirectory(), fileFilter);
        log.info("识别到 {} 个待替换文档",files.size());

        log.info("开始处理文档,计时开启!");
        TimeInterval interval = new TimeInterval();
        interval.start();
        if (!propVO.getShowDetail()){
            log.info("正在处理中...");
        }
        files.stream().forEach(file -> {
            //加载Word文档
            Document document = new Document(file.getPath());
            propVO.getReplaces().forEach((original, update) -> {
                //使用新文本替换文档中的指定文本
                document.replace(original, update, propVO.getCaseSensitive(), true);
            });
            //保存文档
            document.saveToFile(file.getPath(), FileFormat.Docx_2013);
            if (propVO.getShowDetail()){
                log.info("处理完成: {} ,第 {}/{} 个",file.getPath(),++index,files.size());
            }
        });
        Console.log("全部处理完成,共 {} 个文档, 耗时：{}", files.size(), interval.intervalPretty());
    }
}
