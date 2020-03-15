package cn.xiuminglee.vboot.modules.netty;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author Xiuming Lee
 * 用于监听Spring Boot启动完成，然后启动Netty
 */
@Component("nettyBooter")
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null){
            String property = event.getApplicationContext().getEnvironment().getProperty("vboot.netty-server-port");
            int port = Integer.parseInt(property);
            NettyServer.getInstance().start(port);
        }
    }
}
