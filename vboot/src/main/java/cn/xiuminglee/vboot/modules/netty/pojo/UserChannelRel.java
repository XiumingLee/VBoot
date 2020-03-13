package cn.xiuminglee.vboot.modules.netty.pojo;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * @author 22
 * @Description: 用户id和channel的关联关系处理
 */
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
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }
}
