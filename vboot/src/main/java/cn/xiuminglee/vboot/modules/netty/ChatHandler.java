package cn.xiuminglee.vboot.modules.netty;

import cn.xiuminglee.vboot.core.common.utils.JsonUtils;
import cn.xiuminglee.vboot.core.common.utils.SpringContextHolder;
import cn.xiuminglee.vboot.modules.netty.enums.MsgActionEnum;
import cn.xiuminglee.vboot.modules.netty.pojo.MsgContent;
import cn.xiuminglee.vboot.modules.netty.pojo.UserChannelRel;
import cn.xiuminglee.vboot.modules.system.entity.ChatMsg;
import cn.xiuminglee.vboot.modules.system.service.ChatMsgService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static Logger log = LoggerFactory.getLogger(ChatHandler.class);

    /**
     *用于记录和管理所有客户端(或理解为用户)的channle
     */
    private static ChannelGroup clients =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
            throws Exception {
        //通过SpringContextHolder从Spring容器中获取ChatMsgService
        ChatMsgService chatMsgService = SpringContextHolder.getBean(ChatMsgService.class);
        /**
         * 步骤：
         * 1、获取客户端发来的消息
         * 2、判断消息类型，根据不同的类型来处理不同的业务
         * 2.1、当websocket 第一次open的时候，初始化channel，把用的channel和userid关联起来
         * 2.2、聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
         * 2.3、签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
         * 2.4、心跳类型的消息
         * 2.5、系统通知类型，向所有用户推送消息
         */

        // 1. 获取客户端发来的消息 ，并将其进行类型转换。
        String content = msg.text();
        MsgContent dataContent = JsonUtils.jsonToPojo(content,MsgContent.class);
        // 2. 判断消息类型，根据不同的类型来处理不同的业务
        //获取消息中的类型
        Integer action = dataContent.getAction();
        //获取当前channel
        Channel currentChannel = ctx.channel();
        // 	2.1  当websocket 第一次open连接的时候，将该用户客户端连接的channel和userid关联起来
        if (action.equals(MsgActionEnum.CONNECT.type)){
            //从消息中获取用户id
            Integer sendUserId = dataContent.getChatMsg().getSendUserId();
            //将channel和userid关联起来
            UserChannelRel.put(sendUserId, currentChannel);
            UserChannelRel.output();
        }
        //  2.2  聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
        else if (action.equals(MsgActionEnum.CHAT.type)) {
            ChatMsg chatMsg = dataContent.getChatMsg();
            //签收状态未签收
            chatMsg.setSignFlag(0);
            chatMsg.setCreateTime(new Date());
            boolean insert = chatMsgService.insert(chatMsg);
            //发送消息
            //通过消息中的接收方ID获取接收方的信道
            Channel receiverChannel = UserChannelRel.get(chatMsg.getReceiveUserId());
            if (receiverChannel == null) {
                // TODO channel为空代表用户离线，推送消息（JPush，个推，小米推送）
            } else {
                receiverChannel.writeAndFlush(
                        new TextWebSocketFrame(JsonUtils.objectToJson(chatMsg)));
            }

        }
        // 2.3  签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
        else if(action.equals(MsgActionEnum.SIGNED.type)){
            ChatMsg chatMsg = dataContent.getChatMsg();
            //签收状态已签收
            chatMsg.setSignFlag(1);
            chatMsgService.updateById(chatMsg);
        }

        else if(action.equals(MsgActionEnum.KEEPALIVE.type)){
            log.info("收到来自channel为{}的心跳包...",currentChannel);
        }
    }
    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channle，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开，channle对应的短id为：{}",ctx.channel().id().asShortText());
        clients.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        clients.remove(ctx.channel());
    }
}
