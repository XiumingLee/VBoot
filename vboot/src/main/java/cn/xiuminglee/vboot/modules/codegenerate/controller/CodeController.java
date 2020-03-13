package cn.xiuminglee.vboot.modules.codegenerate.controller;

import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.modules.codegenerate.entity.GenerateCode;
import cn.xiuminglee.vboot.modules.codegenerate.service.TableService;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @Author Xiuming Lee
 * 代码生成控制器
 */
@RestController
@RequestMapping("/system/code")
public class CodeController {
    @Autowired
    private TableService tableService;

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String dbDriverName;

    /**
     * 获取数据库中的多有表
     * @return
     */
    @GetMapping("/tables")
    public SimpleResponse getAllTable(){
        List<Map<String, Object>> allTables = tableService.getAllTables();
        return new SimpleResponse(200,"",allTables);
    }

    @PostMapping("/generate")
    public SimpleResponse generateCode(@RequestBody GenerateCode generateCode){
        generateByTables(generateCode);
        return new SimpleResponse(200,"生成代码成功！");
    }

    /**
     * 代码生成
     */
    private void generateByTables(GenerateCode generateCode){
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(dbUsername)
                .setPassword(dbPassword)
                .setDriverName(dbDriverName);
        /**
         * 策略配置项
         */
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                //表前缀
                .setTablePrefix(generateCode.getTablePrefix())
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
                //修改替换成你需要的表名，多个表名传数组
                .setInclude(generateCode.getTableNames());
        /**
         *全局配置
         */
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(false)
                //关闭二级缓存
                .setEnableCache(false)
                //开启BaseResultMap
                .setBaseResultMap(true)
                .setAuthor(generateCode.getAuthor())
                .setOutputDir(generateCode.getCodePath())
                .setFileOverride(true)
                .setIdType(IdType.AUTO);
        config.setServiceName("%sService");
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(generateCode.getPackageName())
                                .setController("controller")
                                .setEntity("entity")
                ).execute();
    }
}
