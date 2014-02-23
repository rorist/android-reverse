package ch.fixme.workshop0;

import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import java.lang.StringBuilder;
import java.lang.Byte;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogArray(new Byte[]{1,3,3,7});
        LogObject(new String("TEST"));
    }

    private void LogArray(Object[] arr){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<arr.length; i++){
            sb.append(arr[i]).append('\n');
        }
        Log.v("KEY", sb.toString());
    }

    private void LogObject(Object obj) {
        Log.v("KEY", obj.toString());
    }

    private void LogLong(long l) {
        Log.v("KEY", String.valueOf(l));
    }

    private void PrivateDeadMethod(){
        Toast.makeText(getApplicationContext(), "Private Dead", Toast.LENGTH_SHORT).show();
    }

    static public void StaticDeadMethod(){
        Toast.makeText(null, "Static Dead", Toast.LENGTH_SHORT).show();
    }

}
