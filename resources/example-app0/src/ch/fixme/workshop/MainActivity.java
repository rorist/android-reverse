package ch.fixme.workshop0;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import java.lang.StringBuilder;
import java.lang.Byte;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogArray(new Byte[]{1,3,3,7});
    }

    private void LogArray(Object[] arr){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<arr.length; i++){
            sb.append(arr[i].toString());
        }
        Log.v("KEY", sb.toString());
    }

}
