package ch.fixme.workshop2;

import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.util.Log;
import java.security.SecureRandom;
import java.math.BigInteger;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.check_btn).setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    if(checkSerial()){
                        ((TextView)findViewById(R.id.output)).setText("CONGRATZ!");
                    }
                }
            });
    }

    private boolean checkSerial(){
        String serial = ((EditText)findViewById(R.id.input)).getText().toString();
        if (getSerial().equals(serial)) return true;
        return false;
    }

    private String getSerial() {
        SecureRandom random = new SecureRandom(new byte[]{1,3,3,7});
        return new BigInteger(130, random).toString(32);
    }

}
