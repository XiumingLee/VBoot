package cn.xiuminglee.vboot.config;

import cn.xiuminglee.vboot.core.common.utils.QiniuUtil;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Xiuming Lee
 * @Description
 */
@Configuration
@EnableConfigurationProperties(VBootProperties.class)
@MapperScan("cn.xiuminglee.vboot.modules.system.mapper*")
public class VBootConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "vboot", name = "file-upload-type", havingValue = "qiniu")
    public QiniuUtil qiniuUtil(){
        return new QiniuUtil();
    }
}
