import store from '@/store'

const localStorage = window.localStorage;

export const chat = {

    // currentUser:state.user.info,
    /**
	 * 和后端的枚举对应
	 */
    CONNECT: 1, 	// 第一次(或重连)初始化连接
    CHAT: 2, 		// 聊天消息
    SIGNED: 3, 		// 消息签收
    KEEPALIVE: 4, 	// 客户端保持心跳


    ChATMSGS:"chatMsgs",//保存聊天记录的key
    /**
     * 聊天内容
     * @param {*} id 
     * @param {*} sendUserId 
     * @param {*} receiveUserId 
     * @param {*} msg 
     */
    chatMsg: function (id, sendUserId, receiveUserId, msg) {
        this.id = id;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.msg = msg;
    },
    /**
     * 包装聊天内容
     * @param {number} action 
     * @param {object} chatMsg 
     * @param {string} extand 
     */
    MsgContent: function (action, chatMsg, extand) {
        this.action = action;
        this.chatMsg = chatMsg;
        this.extand = extand;
    },

    /**
     * 将聊天信息保存到localstorage
     * @param {*} meId 
     * @param {*} otherId 
     * @param {*} msg 
     */
    setChatMsgsToLocalStorage: function (meId, otherId, msg) {
        let chatKeys = meId + "_" + otherId;       
        let currentChatMsgArr = [];
        let chatMsgsJson = localStorage.getItem(this.ChATMSGS);
        let chatMsgs={};
        if (chatMsgsJson) {
            chatMsgs = JSON.parse(chatMsgsJson);
            if (chatKeys in chatMsgs) {
                currentChatMsgArr = chatMsgs[chatKeys];
            }
        }
        currentChatMsgArr.push(msg);
        chatMsgs[chatKeys] = currentChatMsgArr;
        localStorage.setItem(this.ChATMSGS, JSON.stringify(chatMsgs));
    },

    
    /**
     * 在好友列表标记有未读信息或已读
     * @param {*} key 
     * @param {*} friendId 
     * @param {Number} status
     */
    signHasMsg(userId,friendId,status){
        //获取好友列表
        let friendList = JSON.parse(localStorage.getItem(userId+"_friends"));
        for (let i = 0; i < friendList.length; i++) {
            const friend = friendList[i];
            if (friendId==friend.id) {
                friend.isRead = status;
                friendList.splice(i, 1);
                friendList.unshift(friend);
            }
            
        }
        localStorage.setItem(userId+"_friends",JSON.stringify(friendList));
    }

}