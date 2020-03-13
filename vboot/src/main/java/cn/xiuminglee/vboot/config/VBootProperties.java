package cn.xiuminglee.vboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Xiuming Lee
 * @Description
 */
@ConfigurationProperties(prefix = "vboot")
@Data
public class VBootProperties {

    private QiniuProperties qiniu = new QiniuProperties();

    /** 文件上传类型，默认上传到数据库，后期开发 */
    private String fileUploadType = "default";


    @Data
    public class QiniuProperties{
        private String accessKey;
        private String secretKey;
        private String prefixPath;
        private String bucketName;
        private String folder;
    }
}
