package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.modules.system.entity.ChatMsg;
import cn.xiuminglee.vboot.modules.system.mapper.ChatMsgMapper;
import cn.xiuminglee.vboot.modules.system.service.ChatMsgService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 聊天记录表 服务实现类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-10-10
 */
@Service
public class ChatMsgServiceImpl extends ServiceImpl<ChatMsgMapper, ChatMsg> implements ChatMsgService {
}
