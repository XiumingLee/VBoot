package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.modules.system.entity.Log;
import cn.xiuminglee.vboot.modules.system.mapper.LogMapper;
import cn.xiuminglee.vboot.modules.system.service.LogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 错误日志表 服务实现类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-28
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}
