package cn.xiuminglee.vboot.core.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Author Xiuming Lee
 */
public interface CodeGenerate {
    Code generator(ServletWebRequest webRequest,String number);
}
