package cn.xiuminglee.vboot.modules.system.controller;

import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.VBootUtils;
import cn.xiuminglee.vboot.core.security.authentication.MyUserDetails;
import cn.xiuminglee.vboot.modules.system.entity.ChatMsg;
import cn.xiuminglee.vboot.modules.system.entity.User;
import cn.xiuminglee.vboot.modules.system.service.ChatMsgService;
import cn.xiuminglee.vboot.modules.system.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Xiuming Lee
 */
@RequestMapping("/chat")
@RestController
public class ChatController {
    @Autowired
    private UserService userService;
    @Autowired
    private ChatMsgService chatMsgService;

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 查询所有好友
     * @return
     */
    @GetMapping("/allfriends")
    public SimpleResponse allFriends(){
        String[] selectFields = {"id","name","username","avatar","sex","mobilephone","mail","address"};
        MyUserDetails userDetails = VBootUtils.currentUserInfo();
        List<User> userList = userService.selectList(new EntityWrapper<User>().setSqlSelect(selectFields).ne("id", userDetails.getId()));
        return new SimpleResponse(200,"",userList);
    }

    /**
     * 查询为签收的消息
     * @return
     */
    @GetMapping("/chatMsgs/notsigned")
    public SimpleResponse chatMsgsWithNotSigned(){
        MyUserDetails currentUserInfo = VBootUtils.currentUserInfo();
        List<ChatMsg> Msgs = chatMsgService.selectList(new EntityWrapper<ChatMsg>().eq("receive_user_id", currentUserInfo.getId()).eq("sign_flag",0));
        return new SimpleResponse(200,"",Msgs);
    }

    /**
     * 批量签收消息。
     * @param chatMsgs
     * @return
     */
    @PostMapping("/batchSignIn")
    public SimpleResponse batchSignIn(@RequestBody ArrayList<ChatMsg> chatMsgs){
        log.info(String.valueOf(chatMsgs));
        chatMsgs.forEach(chatMsg -> {
            chatMsg.setSignFlag(1);
        });
        boolean b = chatMsgService.updateBatchById(chatMsgs);
        return new SimpleResponse(200,"签收成功！");
    }

}
