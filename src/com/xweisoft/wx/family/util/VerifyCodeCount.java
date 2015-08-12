package com.xweisoft.wx.family.util;

import android.os.CountDownTimer;
import android.widget.Button;

import com.xweisoft.wx.family.R;

public class VerifyCodeCount extends CountDownTimer
{
    private Button button;
    
    public VerifyCodeCount(long millisInFuture, long countDownInterval,
            Button button)
    {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }
    
    @Override
    public void onFinish()
    {
        cancel();
        button.setText(R.string.ysh_get_validate_number);
        button.setEnabled(true);
    }
    
    @Override
    public void onTick(long millisUntilFinished)
    {
        button.setEnabled(false);
        button.setText("重新获取（" + (millisUntilFinished / 1000) + "s）");
    }
    
}
