package com.candy.getgpspoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private MediaPlayer mp;
    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SMS_RECEIVED_ACTION.equals(action))
        {
            //获取intent参数
            Bundle bundle=intent.getExtras();
            //判断bundle内容
            if (bundle!=null)
            {
                //取pdus内容,转换为Object[]
                Object[] pdus=(Object[])bundle.get("pdus");
                //解析短信
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i=0;i<messages.length;i++)
                {
                    byte[] pdu=(byte[])pdus[i];
                    messages[i]=SmsMessage.createFromPdu(pdu);
                }
                //解析完内容后分析具体参数
                for(SmsMessage msg:messages)
                {
                    //获取短信内容
                    String content=msg.getMessageBody();
                    String sender=msg.getOriginatingAddress();
                    Date date = new Date(msg.getTimestampMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sendTime = sdf.format(date);
                    Intent baiduLocate =new Intent(BaiduMapService.INTENAL_ACTION_BAIDU);
                    if(content.contains("Locate"))
                    {
                        if(content.contains("回复")){
                           baiduLocate.putExtra("send",true);
                        }
                        Toast.makeText(context, "定位", Toast.LENGTH_SHORT).show();
                        abortBroadcast();
                        deleteSms(context,content);
                        baiduLocate.putExtra("code","locate");
                        baiduLocate.putExtra("sender",sender);
                        context.sendBroadcast(baiduLocate);
//                        Intent takepicture = new Intent(context,Main3Activity.class);
//                        takepicture.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(takepicture);
                    }else if(content.contains("Follow"))
                    {
                        Toast.makeText(context, "跟踪", Toast.LENGTH_SHORT).show();
                        abortBroadcast();
                        deleteSms(context,content);
                        baiduLocate.putExtra("code","follow");
                        context.sendBroadcast(baiduLocate);
                    }else if(content.contains("Cancel"))
                    {
                        Toast.makeText(context, "取消跟踪", Toast.LENGTH_SHORT).show();
                        abortBroadcast();
                        deleteSms(context,content);
                        baiduLocate.putExtra("code","stopfollow");
                        context.sendBroadcast(baiduLocate);

                    }else if(content.contains("Aleter")) {
                        Toast.makeText(context, "报警", Toast.LENGTH_SHORT).show();
                        abortBroadcast();
                        deleteSms(context, content);
                        baiduLocate.putExtra("code",content);
                        context.sendBroadcast(baiduLocate);
                    }
                }

            }
        }//if 判断广播消息结束
    }

    public void deleteSms(Context context, String smsContent){
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor isRead = context.getContentResolver().query(uri, null,"read=" + 0, null , null );
        while(isRead.moveToNext()){
            String body = isRead.getString(isRead.getColumnIndex("body")).trim();
            if(body.equals(smsContent)){
                int id = isRead.getInt(isRead.getColumnIndex("_id"));
                context.getContentResolver().delete(Uri.parse("content://sms"), "_id="+id,null);
            }
        }
    }
}
