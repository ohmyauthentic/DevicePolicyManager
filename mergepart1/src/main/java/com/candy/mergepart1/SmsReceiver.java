package com.candy.mergepart1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
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

                    if(content.contains("AntiSteal")){
                        abortBroadcast();
                        deleteSms(context,content);
                        Toast.makeText(context,"防盗",Toast.LENGTH_SHORT).show();
                        Intent mainServiceIntent =new Intent(MainService.INTERNAL_ACTION_MAIN);
                        if(content.contains("回复")){
                            mainServiceIntent.putExtra("send", true);
                        }
                        mainServiceIntent.putExtra("sender", sender);
                        mainServiceIntent.putExtra("code", content);
                        context.sendBroadcast(mainServiceIntent);
                        if(!MainService.isServiceRunning(context,"MainService")){
                            Intent serviceIntent = new Intent(context, MainService.class);
                            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startService(serviceIntent);
                        };
                    }

                }

            }
        }//if 判断广播消息结束
    }

    public void deleteSms(Context context, String smsContent){
        try{
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor isRead = context.getContentResolver().query(uri, null,"read=" + 0, null , null );
        while(isRead.moveToNext()){
            String body = isRead.getString(isRead.getColumnIndex("body")).trim();
            if(body.equals(smsContent)){
                int id = isRead.getInt(isRead.getColumnIndex("_id"));
                context.getContentResolver().delete(Uri.parse("content://sms"), "_id="+id,null);
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
