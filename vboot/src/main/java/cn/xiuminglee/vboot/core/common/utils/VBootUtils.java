package cn.xiuminglee.vboot.core.common.utils;

import cn.xiuminglee.vboot.core.security.authentication.MyUserDetails;
import cn.xiuminglee.vboot.modules.system.entity.Log;
import cn.xiuminglee.vboot.modules.system.entity.User;
import cn.xiuminglee.vboot.modules.system.service.LogService;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

/**
 * @Author Xiuming Lee
 * VBoot系统工具类
 */
public class VBootUtils {


    private static PasswordEncoder passwordEncoder = SpringContextHolder.getBean(PasswordEncoder.class);

    /**
     * 添加或注册用户时，为其密码加密
     *
     * @param user
     * @return
     */
    public static User userPasswordEncoder(User user) {
        User returnUser = user;
        returnUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return returnUser;
    }

    /**
     * 字符串加密
     *
     * @param rawPassword
     * @return
     */
    public static String stringEncoder(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 比较两个密码是否相等
     *
     * @param rawPassword     一般是前端传来的值，不加密的。
     * @param encodedPassword 数据库中保存的值，加密后的。
     * @return
     */
    public static Boolean encoderCompared(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Map<String, Object>类型转换为Bean实体类
     *
     * @param map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * List<Map<String,Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        List<T> list = new ArrayList<T>();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }


    /*******************************************异常处理相关*******************************************************/
    /**
     * 从Spring上下文获取
     * SpringContextHolder自己写的获取Spring上下文的工具类
     */
    private static LogService logService = SpringContextHolder.getBean(LogService.class);

    /**
     * 保存错误日志
     * @return
     */
    public static void errLogSave(Log log) {
        log.setCreatedata(new Date());
        logService.insert(log);
    }

    /**
     * 异常错误的堆栈信息转String
     * @param stackArray   异常的StackTraceElement[]堆栈细信息
     * @return
     */
    public static String stackTraceElementToString(StackTraceElement[] stackArray) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }
    /*******************************************异常处理相关结束*******************************************************/
    /**
     * 获取当前登录用户的信息.
     * @return
     */
    public static MyUserDetails currentUserInfo(){
        //获取当前登录的用户信息
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails;
    }

}
