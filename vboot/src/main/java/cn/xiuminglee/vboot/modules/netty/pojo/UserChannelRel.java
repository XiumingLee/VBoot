package cn.xiuminglee.vboot.modules.netty.pojo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @Author Xiuming Lee
 * @Description: 用户id和channel的关联关系处理
 */
@Slf4j
public class UserChannelRel {
    private static HashMap<Integer, Channel> manager = new HashMap<Integer, Channel>();

    public static void put(Integer senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    public static Channel get(Integer senderId) {
        return manager.get(senderId);
    }

    public static void output() {
        for (HashMap.Entry<Integer, Channel> entry : manager.entrySet()) {
            log.info("UserId: {}, ChannelId: {}" ,entry.getKey(), entry.getValue().id().asLongText());
        }
    }
}
