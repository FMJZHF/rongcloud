package cn.sa.im.ui.Listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.sa.im.MainActivity;
import cn.sa.im.R;
import cn.sa.im.ui.activity.MeetActivity;
import cn.sa.im.ui.activity.UserDetailActivity;
import cn.sa.im.ui.widget.plugin.ApkMessage;
import io.rong.common.RLog;
import io.rong.imageloader.utils.L;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongMessageItemLongClickActionManager;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.tools.CharacterParser;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.widget.provider.MessageItemLongClickAction;
import io.rong.imlib.IHandler;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RecallNotificationMessage;

public class MyConversationClickListener implements RongIM.ConversationClickListener {


    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;

    /**
     * 当点击用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @param targetId         会话 id
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String targetId) {
        Log.i("TAG",userInfo.getUserId());
        if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE || conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
            return false;
        }
        //开发测试时,发送系统消息的userInfo只有id不为空
        if (userInfo != null && userInfo.getName() != null && userInfo.getPortraitUri() != null) {
            //Intent intent = new Intent(context, UserDetailActivity.class);
            //intent.putExtra("conversationType", conversationType.getValue());

            //intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
            //context.startActivity(intent);
        }
        return true;
    }

    /**
     * 当长按用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param user             被点击的用户的信息。
     * @param targetId         会话 id
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo user, String targetId) {
        RongMentionManager.getInstance().mentionMember(conversationType,targetId, user.getUserId());
        //Toast.makeText(context, "长按消息时执行。", Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * 当点击消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被点击的消息的实体信息。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        Log.i("TAG", message.getMessageId() + "<<<");
        if(message.getContent() instanceof ApkMessage) {

        }
        return false;
    }

    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @param message 被点击的消息的实体信息
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link, Message message) {

        return true;
    }

    /**
     * 当长按消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被长按的消息的实体信息。
     * @return 如果用户自己处理了长按后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLongClick(final Context context, View view, final Message message) {

        //setMessageItemLongClickAction(context);


//        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
//        LayoutInflater factory = LayoutInflater.from(context);
//
//        final View textEntryView = factory.inflate(R.layout.dialog_conversation, null);
//        TextView resultOne=(TextView)textEntryView.findViewById(R.id.resultOne); //resultone is a textview in xml dialog
//
//        alert.setView(textEntryView);
//        final AlertDialog dialog=alert.show();
//        textEntryView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        String[] items = new String[]{view.getContext().getResources().getString(R.string.rc_dialog_item_message_delete)};
        /**
         * newInstance() 初始化OptionsPopupDialog
         * @param items弹出菜单功能选项
         * setOptionsPopupDialogListener()设置点击弹出菜单的监听
         * @param which表示点击的哪一个菜单项,与items的顺序一致
         * show()显示pop dialog
         */
        OptionsPopupDialog.newInstance(view.getContext(), items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {

            @Override
            public void onOptionsItemClicked(int which) {
                if (which == 0) {
                   RongIMClient.getInstance().recallMessage(message, getPushContent(context, message), new RongIMClient.ResultCallback<RecallNotificationMessage>() {
                       @Override
                       public void onSuccess(RecallNotificationMessage recallNotificationMessage) {

                       }

                       @Override
                       public void onError(RongIMClient.ErrorCode errorCode) {

                       }
                   });
                }
            }
        }).show();


        return false;
    }

    private static void setMessageItemLongClickAction(Context context) {
        MessageItemLongClickAction action = new MessageItemLongClickAction.Builder()
                .titleResId(R.string.sa_dialog_item_message_delete)
                .actionListener(new MessageItemLongClickAction.MessageItemLongClickListener() {
                    @Override
                    public boolean onMessageItemLongClick(Context context, UIMessage message) {
                        Message[] messages = new Message[1];
                        messages[0] = message.getMessage();
                        if (message.getConversationType().equals(Conversation.ConversationType.PRIVATE)) {
                            RongIM.getInstance().deleteRemoteMessages(message.getConversationType(), message.getTargetId(), messages, null);
                        } else {
                            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
                        }
                        return false;
                    }
                }).build();
        RongMessageItemLongClickActionManager.getInstance().addMessageItemLongClickAction(action, 1);
    }
    private String getPushContent(Context context, Message message) {
        String userName = "";
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());
        if (userInfo != null) {
            userName = userInfo.getName();
        }
        return context.getString(io.rong.imkit.R.string.rc_user_recalled_message, userName);
    }
}
