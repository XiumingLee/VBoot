package cn.xiuminglee.vboot.modules.system.controller;

import cn.xiuminglee.vboot.config.VBootProperties;
import cn.xiuminglee.vboot.core.common.exception.BusinessException;
import cn.xiuminglee.vboot.core.common.utils.QiniuUtil;
import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.VBootUtils;
import cn.xiuminglee.vboot.core.security.authentication.MyUserDetails;
import cn.xiuminglee.vboot.modules.system.entity.Role;
import cn.xiuminglee.vboot.modules.system.entity.User;
import cn.xiuminglee.vboot.modules.system.entity.UserDept;
import cn.xiuminglee.vboot.modules.system.entity.UserRole;
import cn.xiuminglee.vboot.modules.system.service.RoleService;
import cn.xiuminglee.vboot.modules.system.service.UserDeptService;
import cn.xiuminglee.vboot.modules.system.service.UserRoleService;
import cn.xiuminglee.vboot.modules.system.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private VBootProperties vBootProperties;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserDeptService userDeptService;
    @Autowired(required = false)
    private QiniuUtil qiniuUtil;

    /**
     * 查询所有用户信息，用于在用户管理界面显示在列表位置。
     * 采用分页查询
     * @return
     */
    @GetMapping("/users")
    public SimpleResponse allUsers(@RequestParam Integer page){
        Page<Map> mapPage = userService.selectAllUserPortion(new Page<Map>(page, 10));
        return new SimpleResponse(200,"",mapPage);
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("")
    public SimpleResponse addUser(@RequestBody User user){
        int count = userService.selectCount(new EntityWrapper<User>().eq("username", user.getUsername()));
        if (count>0){
            return new SimpleResponse(999,"用户名已存在，换个用户名试试吧！");
        }
        User passwordEncoder = VBootUtils.userPasswordEncoder(user);
        passwordEncoder.setName("匿名用户");
        boolean b = userService.insert(passwordEncoder);
        if (b){
            return new SimpleResponse(200,"新增用户成功！");
        }
        return new SimpleResponse(999,"添加用户失败！");
    }

    /**
     * get RoleInfo By UserId
     * @param id
     * @return
     */
    @GetMapping("/role/{id}")
    public SimpleResponse getRoleInfoByUserId(@PathVariable Integer id){
        UserRole userRole = userRoleService.selectOne(new EntityWrapper<UserRole>().eq("uid", id));
        Integer rid = userRole.getRid();
        Role role = roleService.selectById(rid);
        return new SimpleResponse(200,"",role.getId());
    }

    /**
     * 赋予用户角色和部门
     * @param map
     * @return
     */
    @PostMapping("/roleDept")
    public SimpleResponse updateUserRole(@RequestBody Map map){
        Integer id = (Integer) map.get("id");
        Integer roleId = (Integer) map.get("roleId");
        Integer deptId = (Integer) map.get("deptId");
        StringBuilder stringBuilder = new StringBuilder();
        if (roleId != null){
            UserRole userRole = new UserRole();
            userRole.setUid(id);
            userRole.setRid(roleId);
            //查询数据库是否有该用户角色记录
            UserRole uid = userRoleService.selectOne(new EntityWrapper<UserRole>().eq("uid", id));
            boolean rb;
            if(uid != null){
                //如果数据库中的角色id和传来的相同则不需要修改
                if (uid.getRid().equals(roleId)){
                    rb = true;
                }else{
                    rb = userRoleService.update(userRole,new EntityWrapper<UserRole>().eq("uid",id));
                }
            }else {
                rb = userRoleService.insert(userRole);
            }
            if (rb){
                stringBuilder.append("修改角色完成！");
            }else {
                stringBuilder.append("修改角色失败！");
            }
        }
        if (deptId != null){
            UserDept userDept = new UserDept();
            userDept.setUid(id);
            userDept.setDid(deptId);
            UserDept uid = userDeptService.selectOne(new EntityWrapper<UserDept>().eq("uid", id));
            boolean db;
            if (uid != null){
                //如果数据库中的部门id和传来的相同，则不需要修改。
                if (uid.getDid().equals(deptId)){
                    db = true;
                }else {
                    db = userDeptService.update(userDept,new EntityWrapper<UserDept>().eq("uid",id));
                }
            }else {
               db = userDeptService.insert(userDept);
            }
            if (db){
                stringBuilder.append("修改部门完成！");
            }else {
                stringBuilder.append("修改部门失败！");
            }
        }
        return new SimpleResponse(200,stringBuilder.toString());
    }


    /**
     * 获取当前用户信息，用于个人资料的展示。
     * @return
     */
    @GetMapping("/me/current")
    public SimpleResponse getCurrentUserInfo(){
        //获取当前登录的用户信息
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User selectOne = userService.selectUserInfoNoPass(userDetails.getId());
        logger.info(String.valueOf(selectOne));
        return new SimpleResponse(200,"",selectOne);
    }

    /**
     * 个人资料修改区域
     */
    /**
     * 修改个人资料
     * @param user
     * @return
     */
    @PutMapping("/me/info")
    public SimpleResponse updateInfo(@RequestBody User user){
        logger.info(String.valueOf(user));
        boolean updateById = userService.updateById(user);
        if (updateById){
            return new SimpleResponse(200,"修改个人资料成功！");
        }else {
            return new SimpleResponse(999,"修改个人资料失败！");
        }
    }

    /**
     * 修改个人密码
     * @param pwds
     * @return
     */
    @PutMapping("/me/pwd")
    public SimpleResponse updatePwd(@RequestBody Map pwds){
        logger.info(String.valueOf(pwds));
        //获取各密码
        String password = (String) pwds.get("password");
        String newassword = (String) pwds.get("newpassword");
        String surepassword = (String) pwds.get("surepassword");
        if (StringUtils.isEmpty(password)||StringUtils.isEmpty(newassword)||StringUtils.isEmpty(surepassword)){
            return  new SimpleResponse(999,"请填全信息！");
        }
        if (!(StringUtils.equals(newassword,surepassword))){
            return new SimpleResponse(999,"两次输入的密码不一样！");
        }
        //获取当前登录的用户信息
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取用户详细信息
        User userInfoByDB = userService.selectOne(new EntityWrapper<User>().eq("id", userDetails.getId()));
        //比较原始密码是否与数据库中的一样
        if (!(VBootUtils.encoderCompared(password,userInfoByDB.getPassword()))){
            logger.info(userInfoByDB.getPassword());
            return new SimpleResponse(999,"你输入的原密码错误！");
        }
        //更新密码
        User user = new User();
        user.setId(userInfoByDB.getId());
        user.setPassword(VBootUtils.stringEncoder(newassword));
        boolean b = userService.updateById(user);
        if (b){
            return new SimpleResponse(200,"修改密码成功！");
        }else {
            return new SimpleResponse(999,"修改密码失败！");
        }
    }

    /**
     * 更新头像
     * @param file
     * @return
     */
    @PostMapping("/me/avatar")
    public SimpleResponse uploadAvatar(MultipartFile file){
        //获取当前登录的用户信息
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取用户以前的头像
        User userDB = userService.selectOne(new EntityWrapper<User>().eq("id", userDetails.getId()));
        String oldAvatar = userDB.getAvatar();

        Map json = Maps.newHashMap();
        //源文件名
        String originalFilename = file.getOriginalFilename();
        byte[] bytes;
        String uploadName;
        try {
            // 获取文件的bytes形式
            bytes = file.getBytes();
            //调用QiniuUtil中的方法
            uploadName = qiniuUtil.upload(bytes, originalFilename);
            json.put("success", true);
            json.put("file_path", uploadName);
            //更新数据库
            User user = new User();
            user.setId(userDetails.getId());
            user.setAvatar(uploadName);
            userService.updateById(user);
        } catch (Exception e) {
            json.put("success", false);
            throw new BusinessException(999,"上传头像到七牛云失败！");
        }

        if(!(StringUtils.isEmpty(oldAvatar))){
            String s = StringUtils.substringAfter(oldAvatar, vBootProperties.getQiniu().getPrefixPath());
            Boolean aBoolean = qiniuUtil.deleteByKey(s);
            if (aBoolean){
                return new SimpleResponse(200,"上传成功并删除旧头像",json);
            }else {
                return new SimpleResponse(200,"上传成功但删除旧头像失败",json);
            }
        }
        return new SimpleResponse(200,"更新头像成功！",json);
    }

}
