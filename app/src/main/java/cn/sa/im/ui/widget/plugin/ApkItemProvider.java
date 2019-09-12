package cn.sa.im.ui.widget.plugin;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.sa.im.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

@ProviderTag(
        messageContent = ApkMessage.class,
        showReadState = true
)
public class ApkItemProvider extends IContainerItemProvider.MessageProvider<ApkMessage> {

    public ApkItemProvider() {
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        //这就是展示在会话界面的自定义的消息的布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_apk_message, null);
        ViewHolder holder = new ViewHolder();
        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        holder.tvStoreName = (TextView) view.findViewById(R.id.tv_store_name);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, ApkMessage apkMessage, UIMessage message) {

        //根据需求，适配数据
        ViewHolder holder = (ViewHolder) view.getTag();

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        Log.i("TAG",""+message.getMessageId()+"{{{{{");
        Log.i("TAG",apkMessage.getExtra()+"}}}}}}");
        Log.i("TAG",message.getExtra()+"@@@@@@@");

        if(message.getExtra()!=null&&!message.getExtra().equals("")) {
            if ("isopen".equals(message.getExtra())) {
                holder.tvTitle.setText("¥1.00");
                holder.tvTitle.setTextColor(Color.RED);
                holder.tvStoreName.setText("转账给你");
            } else {
                holder.tvTitle.setText("¥1.00");
                holder.tvStoreName.setText("转账给你");
            }
        }else{
            holder.tvTitle.setText("¥1.00");
            holder.tvStoreName.setText("转账给你");
        }

    }

    @Override
    public Spannable getContentSummary(ApkMessage redPackageMessage) {
        return null;
    }

    @Override
    public void onItemClick(View view, int i, ApkMessage redPackageMessage, UIMessage uiMessage) {
        uiMessage.setExtra("isopen");
        RongIMClient.getInstance().setMessageExtra(uiMessage.getMessageId(), "isopen", null);
        RongContext.getInstance().getEventBus().post(uiMessage);
    }

    @Override
    public void onItemLongClick(final View view, int i, ApkMessage redPackageMessage, UIMessage uiMessage) {

    }

    private static class ViewHolder {
        TextView tvTitle, tvStoreName;
    }
}
