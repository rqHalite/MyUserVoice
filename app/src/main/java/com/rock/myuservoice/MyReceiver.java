package com.rock.myuservoice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Rock on 2018/4/4.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_SHOW_SHOW_AT_MOST = 3;   //推送通知最多显示条数
        private static final String TAG = "JPush";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            switch (intent.getAction()){

                case JPushInterface.ACTION_REGISTRATION_ID:
                    String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                    Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                    break;
                case JPushInterface.ACTION_MESSAGE_RECEIVED:
                    Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                    processCustomMessage(context, bundle);
                    break;
                case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                    Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                    int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                    Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                    break;
                case JPushInterface.ACTION_NOTIFICATION_OPENED:
                    Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

                    //打开自定义的Activity
                    Intent i = new Intent(context, TestActivity.class);
                    i.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    context.startActivity(i);
                    break;
                case JPushInterface.ACTION_RICHPUSH_CALLBACK:
                    Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                    //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                    break;
                case JPushInterface.ACTION_CONNECTION_CHANGE :
                    boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                    Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
                    break;
                default:
                    Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
                    break;

            }
//            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//                //send the Registration Id to your server...
//
//            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);
//
//            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//
//            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
//
//                //打开自定义的Activity
//                Intent i = new Intent(context, TestActivity.class);
//                i.putExtras(bundle);
//                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                context.startActivity(i);
//
//            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//
//            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//                Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
//            } else {
//                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//            }
        }


    /**
     * 实现自定义推送声音
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);

        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Intent mIntent = new Intent(context,TestActivity.class);
//        ThirdView.isReadList = false;
        mIntent.putExtra("sex", "");
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);

        notification.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(msg)
                .setContentTitle(title.equals("") ? "title": title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setNumber(NOTIFICATION_SHOW_SHOW_AT_MOST);
//        notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.test));
        Log.e(TAG, "processCustomMessage: extras----->" + extras);
        if (!TextUtils.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (null != extraJson && extraJson.length() > 0) {
                    String sound = extraJson.getString("sound");
                    if("1".equals(sound)){
                        notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.test));
                    } else {
                        notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.test));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_SHOW_SHOW_AT_MOST, notification.build());  //id随意，正好使用定义的常量做id，0除外，0为默认的Notification
    }
//    private void processCustomMessage(Context context, Bundle bundle) {
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//
//
//
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
//
//        notification.setAutoCancel(true)
//                .setContentText("自定义推送声音")
//                .setContentTitle("极光测试")
//                .setSmallIcon(R.mipmap.ic_launcher);
//
//
//
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        if (!TextUtils.isEmpty(extras)) {
//            try {
//                JSONObject extraJson = new JSONObject(extras);
//                if (null != extraJson && extraJson.length() > 0) {
//
//
//                    String sound = extraJson.getString("sound");
//
//                    if("test.mp3".equals(sound)){
//                        notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.test));
//                    }
//
//
//                }
//            } catch (JSONException e) {
//
//            }
//
//        }
//
//
//        Intent mIntent = new Intent(context,TestActivity.class);
//
//        mIntent.putExtras(bundle);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
//
//        notification.setContentIntent(pendingIntent);
//
//
//
//        notificationManager.notify(2, notification.build());
//
//    }
}
