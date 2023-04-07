package com.mudxx.tool.japidocs;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import org.junit.Test;

/**
 * @author laiw
 * @date 2023/4/6 13:50
 */
public class JApiDocsMainDemoBoot {

    private static final String ROOT_PATH = "/Users/laiwen/Documents/JApiDocs";

    @Test
    public void demo() {
        DocsConfig config = new DocsConfig();
        // 项目根目录
        config.setProjectPath("/Users/laiwen/Desktop/work/develop/workspace/mudxx/mall-learning-projects/mudxx-demo/mudxx-demo-boot");
        // 项目名称
        config.setProjectName("mudxx-demo-boot");
        // 声明该API的版本
        config.setApiVersion("V1.0");
        // 生成API 文档所在目录
        config.setDocsPath(ROOT_PATH + "/mudxx-demo-boot/");
        // 配置自动生成
        config.setAutoGenerate(Boolean.TRUE);
        // 执行生成文档/**/
        Docs.buildHtmlDocs(config);
    }




}
