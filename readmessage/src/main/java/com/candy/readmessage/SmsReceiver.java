package com.candy.readmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
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
                    if(content.contains("ActiveAntiSteal"))
                    {
                        Toast.makeText(context, "激活设备", Toast.LENGTH_LONG).show();
                        abortBroadcast();
                        deleteSms(context,content);
                        Intent takepicture = new Intent(context,Main3Activity.class);
                        takepicture.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(takepicture);
                    }else if(content.contains("ActiveAntiSteal"))
                    {

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
