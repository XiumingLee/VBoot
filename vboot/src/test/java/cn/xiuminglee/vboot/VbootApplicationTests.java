package cn.xiuminglee.vboot;

import cn.xiuminglee.vboot.core.security.rbac.RBACEntity;
import cn.xiuminglee.vboot.modules.system.entity.Menu;
import cn.xiuminglee.vboot.modules.system.service.DictService;
import cn.xiuminglee.vboot.modules.system.service.MenuService;
import cn.xiuminglee.vboot.modules.system.service.UserRoleService;
import cn.xiuminglee.vboot.modules.system.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VbootApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MenuService menuService;
    @Autowired
    private DictService dictService;
    @Autowired
    private UserRoleService userRoleService;

    private Logger log = LoggerFactory.getLogger(getClass());


    @Test
    public void contextLoads() {
        List<RBACEntity> userUrlById = menuService.findUserUrlById(5);
        System.out.println(userUrlById);
    }


    @Test
    public void dictTest(){
        List<Menu> menuByUserId = menuService.findMenuByUserId(1);
        System.out.print(menuByUserId);
    }
//
//
//    @Test
//    public void menuTest(){
//        List<Map<String, Object>> mapList = menuService.selectMaps(new EntityWrapper<Menu>().ne("id",0));
//        Object warp = new MenuWapper(mapList).warp();
//        System.out.println(warp);
//    }
//
//    @Test
//    public void menuOptions(){
////        List<MenuOption> menuOption = menuService.findMenuOption();
////        System.out.println(menuOption);
//        User user = new User();
//        user.setId(1);
//        user.setPassword(VBootUtils.stringEncoder("123456"));
//        boolean b = userService.updateById(user);
//        System.out.println(b);
//    }
//
//    /**
//     * 分页查询测试
//     */
//    @Test
//    public void pageTest(){
////        Page<Menu> menuPage = menuService.selectPage(new Page<Menu>(1,10), new EntityWrapper<Menu>().ne("id",0));
//////        menuPage.getRecords();
////        System.out.println(menuPage);
//        Integer page =1;
//        Page<Dict> dictPage = dictService.selectPage(new Page<Dict>(page, 10), new EntityWrapper<Dict>().eq("parentid", 0));
//        //类型转换
//        List<Map<String, Object>> mapList = VBootUtils.objectsToMaps(dictPage.getRecords()) ;
////        /**
////         * 调用DictWapper将字典信息重新包装
////         */
//        Object warp = new DictWapper(mapList).warp();
//        PageProcessor pageProcessor = new PageProcessor();
//        pageProcessor.setData(warp);
//        pageProcessor.setTotal(dictPage.getTotal());
//        pageProcessor.setCurrent(dictPage.getCurrent());
//        log.info("warp");
//        System.out.println(pageProcessor);
//    }
//
//    /**
//     * 七牛测试
//     */
//    @Autowired
//    private QiniuUtil qiniuUtil;
//    @Test
//    public void qiniu() throws QiniuException {
//        qiniuUtil.deleteByKey("vboot/avatar657a1028-8fda-476e-b054-b45fb3570003.png");
//    }
//
//    @Test
//    public void testStringUtils(){
//        String abc = "abcdefghij";
//        String abc1 = StringUtils.substringAfter(abc, "abc");
//        System.out.println(abc1);
//    }
//
//    @Test
//    public void logbackTest(){
//        throw new BusinessException(999,"lalal！");
//    }
//
//    @Autowired
//    private TableService tableService;
//
//    @Test
//    public void tableServiceTest(){
//        List<Map<String, Object>> allTables = tableService.getAllTables();
//        System.out.println(allTables);
//    }

//    @Test
//    public void testLoadPropertiesResource() throws Exception {
//        Properties ret = PropertiesLoaderUtils
//                .loadProperties(new ClassPathResource("test.properties"));
//        assertEquals("value", ret.getProperty("key"));
//        assertEquals("中文", ret.getProperty("key2"));
//    }


}
