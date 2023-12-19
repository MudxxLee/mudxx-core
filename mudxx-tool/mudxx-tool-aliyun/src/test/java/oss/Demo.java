package oss;

import com.mudxx.tool.aliyun.oss.OssClientUtil;

/**
 * @author laiw
 * @date 2023/11/22 16:37
 */
public class Demo {

    /**
     * #AgileCDN系统ALi平台账号AK/SK
     * platform.ali.accessKey=LTAI5tRpsE1rQXvbwH3mke9n
     * platform.ali.secretKey=DusEfTexBU6OiZLwuvLTOPOEY7rtZv
     * #oss默认的区域域名
     * platform.ali.endPoint=oss-ap-southeast-1.aliyuncs.com
     *
     * @param args
     */

    public static void main(String[] args) {

        OssClientUtil.downloadFile(
                "oss-ap-southeast-1.aliyuncs.com",
                "LTAI5tRpsE1rQXvbwH3mke9n",
                "DusEfTexBU6OiZLwuvLTOPOEY7rtZv",
                "agilewing-billing-dev-t1-cost",
                "year=2023/month=10/part-00000-a2720052-8e65-46ea-984b-4981fad90b8d-c000.snappy.parquet"
        );

    }

}
