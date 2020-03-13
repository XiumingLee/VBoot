package cn.xiuminglee.vboot.modules.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author Xiuming Lee
 * netty 服务类
 */
@Component("nettyServer")
public class NettyServer {
    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 保证NettyServer为单例。
     */
    private static class  SingletionNttyServer {
        static final NettyServer instance = new NettyServer();
    }
    public static NettyServer getInstance(){
        return SingletionNttyServer.instance;
    }


    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    /**
     * 初始化NettyServer
     */
    public NettyServer(){
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerInitialzer());
    }
    public void start(){
        this.future = server.bind(8088);
        log.info("Netty WebSocket Server 启动成功！");
    }
    /**
     * 此处的优雅关闭已经不需要了，因为我们已经将netty依托给了Spring Boot，由他帮我们管理。
     */
}
