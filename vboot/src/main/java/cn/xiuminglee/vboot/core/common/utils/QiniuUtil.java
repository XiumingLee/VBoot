package cn.xiuminglee.vboot.core.common.utils;

import cn.xiuminglee.vboot.config.VBootProperties;
import cn.xiuminglee.vboot.core.common.exception.BusinessException;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author Xiuming Lee
 */
public class QiniuUtil implements InitializingBean {
    
    @Autowired
    private VBootProperties vBootProperties;

    private Auth auth;
    private Configuration cfg;

    @Override
    public void afterPropertiesSet() throws Exception {
        auth = Auth.create(vBootProperties.getQiniu().getAccessKey(), vBootProperties.getQiniu().getSecretKey());
        cfg = new Configuration(Zone.zone1());
    }


    //普通上传
    public String upload(byte[] bytes, String oldName) throws IOException {
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = new UploadManager(cfg);

        String upToken = auth.uploadToken(vBootProperties.getQiniu().getBucketName());

//        修改文件名称，以免重复
        //文件扩展名
        String fileExtName = oldName.substring(oldName.lastIndexOf(".") + 1);
        String fileName = vBootProperties.getQiniu().getFolder() + UUID.randomUUID() + "." + fileExtName;
        //创建上传对象
        //调用put方法上传  这里是使用的Byte类型上传的。还有其他方法
        Response res = uploadManager.put(bytes, fileName, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
//        获取返回的文件名
        String key = putRet.key;
//        获取图片路径
        String filePath = vBootProperties.getQiniu().getPrefixPath() + key;

        return filePath;
    }

    /**
     * 删除文件
     * @param key
     * @return
     */
    public Boolean deleteByKey(String key) {
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(vBootProperties.getQiniu().getBucketName(), key);
            return true;
        } catch (QiniuException e) {
            e.printStackTrace();
            throw new BusinessException(999,"删除七牛云原有头像失败！");
        }
    }


}
