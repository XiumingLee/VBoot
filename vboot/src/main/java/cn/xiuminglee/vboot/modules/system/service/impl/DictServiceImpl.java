package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.modules.system.entity.Dict;
import cn.xiuminglee.vboot.modules.system.mapper.DictMapper;
import cn.xiuminglee.vboot.modules.system.service.DictService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-01
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
}
