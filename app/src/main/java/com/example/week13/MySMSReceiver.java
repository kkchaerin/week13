package com.example.week13;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MySMSReceiver extends BroadcastReceiver {
    interface OnSmsReceived{//broadcast에서 activity로 수신 메시지를 전달함.
        void onReceived(String msg);
    }
    private OnSmsReceived onSmsReceived = null;
    public void setOnSmsReceived(OnSmsReceived smsReceived){
        onSmsReceived = smsReceived;
    }
    @Override
    public void onReceive(Context context, Intent intent) {//문자 받았을 때 실행하는 코드들
        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
            SmsMessage[] messages =Telephony.Sms.Intents.getMessagesFromIntent(intent);
            if(messages!=null){
                if(messages.length == 0) return;
                StringBuilder sb = new StringBuilder();
                for(SmsMessage smsMessage: messages){
                    sb.append(smsMessage.getOriginatingAddress()); //발신 번호
                    sb.append(" : ");
                    sb.append(smsMessage.getMessageBody()); //문자 내용
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();

                if(onSmsReceived!=null)
                    onSmsReceived.onReceived(message);//activity로 message 전달.
            }
        }
    }
}
