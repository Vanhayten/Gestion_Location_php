package com.example.gestionlocationnew;


import android.os.Handler;
import android.os.Looper;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class sync_mysql {
    boolean checker;

    public boolean  syncAdd(String[] thisField ,String[] thisData ,String hosting)
    {
         checker = false;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = thisField;
                //Creating array for data
                String[] data = thisData;

                PutData putData = new PutData(hosting, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Add Success")){
                            checker = true;
                        }else{
                            checker = false;
                        }
                    }
                }
            }
        });
        return checker;
    }
}
