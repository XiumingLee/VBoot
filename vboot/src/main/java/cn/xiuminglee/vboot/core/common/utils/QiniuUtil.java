package cn.xiuminglee.vboot.core.common.utils;

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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * @author 22
 */
@Component("qiniuUtil")
public class QiniuUtil {
    //设置需要操作的账号的AK和SK
    private static final String ACCESS_KEY = "hkzbRmaxJRoGP9PokwYMN_dvjTdkw5xpO4GhUeNb";
    private static final String SECRET_KEY = "damFtnaFKE8GfvAW5wMT6HCy1bQZRvjwJQlCLG-r";
    public static final String VBOOT_QINIU_PATH = "http://qiniu.xiuminglee.cn/";
    //要上传的空间
    private static final String bucketname = "note";
    //密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    private Configuration cfg = new Configuration(Zone.zone1());
    //普通上传
    public String upload(byte[] bytes, String oldName) throws IOException {
        //构造一个带指定Zone对象的配置类
        UploadManager uploadManager = new UploadManager(cfg);

        String upToken = auth.uploadToken(bucketname);

//        修改文件名称，以免重复
        //文件扩展名
        String fileExtName = oldName.substring(oldName.lastIndexOf(".") + 1);
        String fileName = "vboot/touxiang/" + UUID.randomUUID() + "." + fileExtName;
        //创建上传对象
        //调用put方法上传  这里是使用的Byte类型上传的。还有其他方法
        Response res = uploadManager.put(bytes, fileName, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
//        获取返回的文件名
        String key = putRet.key;
//        获取图片路径
        String filePath = VBOOT_QINIU_PATH+key;

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
            bucketManager.delete(bucketname, key);
            return true;
        } catch (QiniuException e) {
            e.printStackTrace();
            throw new BusinessException(999,"删除七牛云原有头像失败！");
        }
    }

}
