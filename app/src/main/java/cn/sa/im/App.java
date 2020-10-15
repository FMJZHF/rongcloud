package cn.sa.im;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.health.SystemHealthManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.sa.im.ui.Listener.MyConversationClickListener;
import cn.sa.im.ui.Listener.MyConversationListBehaviorListener;
import cn.sa.im.ui.apadper.PrivateConversationProviderEx;
import cn.sa.im.ui.widget.GifMessagesaItemProvider;
import cn.sa.im.ui.widget.RceRecallMessageItemProvider;
import cn.sa.im.ui.widget.TextMessagesaItemProvider;
import cn.sa.im.ui.widget.plugin.ApkExtensionModule;
import cn.sa.im.ui.widget.plugin.ApkItemProvider;
import cn.sa.im.ui.widget.plugin.ApkMessage;
import cn.sa.im.ui.widget.plugin.ContactNotificationMessageProvider;
import cn.sa.im.ui.widget.plugin.CustomizeMessage;
import cn.sa.im.ui.widget.plugin.CustomizeMessageItemProvider;
import cn.sa.im.ui.widget.plugin.InsertItemProvider;
import cn.sa.im.ui.widget.plugin.InsertMessage;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongNotificationManager;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.push.PushType;
import io.rong.push.RongPushClient;
import io.rong.push.notification.PushNotificationMessage;
import io.rong.push.pushconfig.PushConfig;
import io.rong.sight.SightExtensionModule;

public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        PushConfig config = new PushConfig.Builder()
                .enableHWPush(true)  // 配置华为推送
                .build();
        //返回配置的推送类型
        Set<PushType> s= config.getEnabledPushTypes();


        RongPushClient.setPushConfig(config);
        RongIM.setServerInfo("navsg01.cn.ronghub.com","");
        //RongIM.init(this,"pvxdm17jpof6r");
        RongIM.init(this,"8luwapkv84zrl");
        //自定义消息
        RongIM.registerMessageTemplate(new ApkItemProvider());
        RongIM.registerMessageTemplate(new InsertItemProvider());
        RongIM.registerMessageTemplate(new RceRecallMessageItemProvider());
        RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
        RongIM.registerMessageTemplate(new GifMessagesaItemProvider());
        RongIM.registerMessageTemplate(new TextMessagesaItemProvider());
        RongIM.registerMessageTemplate(new CustomizeMessageItemProvider());
        //用来隐藏部分控件自定义 base cell
        RongIM.getInstance().registerConversationTemplate(new PrivateConversationProviderEx());
        RongIM.registerMessageType(ApkMessage.class);
        RongIM.registerMessageType(InsertMessage.class);
        RongIM.registerMessageType(CustomizeMessage.class);

        //小视频
        RongExtensionManager.getInstance().registerExtensionModule(new SightExtensionModule());
        //RongExtensionManager.getInstance().registerExtensionModule(new RCSCallModule());
        RongExtensionManager.getInstance().unregisterExtensionModule(new DefaultExtensionModule());
        //自定义plugin
        RongExtensionManager.getInstance().registerExtensionModule(new ApkExtensionModule());
        //会话列表
        RongIM.getInstance().setConversationListBehaviorListener(new MyConversationListBehaviorListener());
        //会话界面
        RongIM.getInstance().setConversationClickListener(new MyConversationClickListener());
        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目


        //使用消息携带用户信息
        RongIM.getInstance().setMessageAttachedUserInfo(false);
        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                Log.i("TAG",i+"!!!!!!");

            }
        }, Conversation.ConversationType.PRIVATE);
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                if(message.getContent() instanceof ImageMessage){
                    Log.i("TAG",message.toString());
                }

                if (message.getSenderUserId().equals("10003")) {
                    Log.i("TAG", "11");
                    return true;
                }

                if (message.getContent() instanceof InformationNotificationMessage) {
                    int[] nums2 = new int[message.getMessageId()];
                    RongIM.getInstance().deleteMessages(nums2, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
                if (message.getConversationType() == Conversation.ConversationType.SYSTEM) {
                    //做自己的操作  发送本地通知//
                    //Log.i("TAG", message.getContent().getSearchableWord().get(0));
                    //RongNotificationManager.getInstance().onReceiveMessageFromApp(message, 0);
                    Log.i("TAG", message.getUId());
                }
                Log.i("TAG",message.getContent().toString());
                return false;
            }
        });
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {

                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                Log.i("TAG",message.getObjectName()+"@@@@");
                return true;
            }
        });
        RongIMClient.setOnRecallMessageListener(new RongIMClient.OnRecallMessageListener() {
            @Override
            public boolean onMessageRecalled(Message message, RecallNotificationMessage recallNotificationMessage) {
                Log.i("TAG","onMessageRecalled");
                return false;
            }
        });
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static void isPush() {
        RongIMClient.getInstance().setPushContentShowStatus(true, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                Log.i("TAG", "111");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("TAG", errorCode.getMessage() + "111");
            }
        });
    }
}
