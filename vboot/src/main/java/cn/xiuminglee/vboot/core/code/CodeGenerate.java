package cn.xiuminglee.vboot.core.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author 22
 */
public interface CodeGenerate {
    Code generator(ServletWebRequest webRequest,String number);
}
