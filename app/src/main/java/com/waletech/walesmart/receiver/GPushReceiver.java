package com.waletech.walesmart.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.waletech.walesmart.R;
import com.waletech.walesmart.comment.Comment_Act;
import com.waletech.walesmart.publicSet.IntentSet;
import com.waletech.walesmart.publicSet.NoticeSet;
import com.waletech.walesmart.sharedinfo.SharedAction;
import com.waletech.walesmart.user.lockInfo.LockInfo_Act;

/**
 * Created by KeY on 2016/5/25.
 */
public class GPushReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GPushActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("GPush", "onReceive");
        NotificationManager noticeManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");

                if (payload != null) {
                    String data = new String(payload);

                    payloadData.append(data);
                    payloadData.append("\n");

                    // 没有还鞋的时候的result string
                    // 通过"\u0024" 即 "$"
                    if (data.startsWith("\u0024")) {
                        Log.i("GPush", "get data is : " + data);

                        // 设置当有消息是的提示，图标和提示文字
                        String ticker = context.getString(R.string.gpush_no_return_notice_ticker);
                        String title = context.getString(R.string.gpush_no_return_notice_title);
                        String content = data.substring(1, data.length());

                        Notification notification = onCreateNotice(context, ticker, title, content);

                        // 如果已经打开了，当点击通知的时候就不会再打开一个activity
                        if (!LockInfo_Act.isView) {
                            Intent lockinfo_int = new Intent(context, LockInfo_Act.class);

                            TaskStackBuilder builder = TaskStackBuilder.create(context);

                            builder.addParentStack(LockInfo_Act.class);
                            builder.addNextIntent(lockinfo_int);

                            new SharedAction(context).setNoticeEnter(true);

                            notification.contentIntent = builder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        }

                        //发送(显示)通知
                        noticeManager.notify((int) System.currentTimeMillis(), notification);

                    } else if (data.startsWith("\u0025")) {
                        String ticker = "开锁通知";
                        String title = "开锁成功";
                        String content = "已经开锁";

                        Notification notification = onCreateNotice(context, ticker, title, content);
                        noticeManager.notify(NoticeSet.HAS_UNLOCK, notification);

                    } else {
                        String ticker = context.getString(R.string.gpush_has_return_notice_ticker);
                        String title = context.getString(R.string.gpush_has_return_notice_title);
                        String content = context.getString(R.string.gpush_has_return_notice_msg);

                        Notification notification = onCreateNotice(context, ticker, title, content);
                        noticeManager.notify(NoticeSet.HAS_RETURN, notification);

                        Intent comment_int = new Intent(context, Comment_Act.class);
                        comment_int.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        comment_int.putExtra(IntentSet.KEY_EPC_CODE, data);
                        context.startActivity(comment_int);
                    }
                    Log.i("GPush", "snd data is : " + data);
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                Log.i("GPush", "thd cid is : " + cid);
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                Log.i("GPush", "forth is");
                break;

            default:
                break;
        }
    }

    private Notification onCreateNotice(Context context, String ticker, String title, String content) {

//        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle()
//                .setBigContentTitle(title) // 通知标题
//                .bigText(content); // 通知内容（可以两行扩展）

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(ticker) // 当通知来的时候的滚动提醒
                        //.setStyle(style)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

}
