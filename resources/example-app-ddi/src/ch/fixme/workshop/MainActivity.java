package ch.fixme.workshop3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View v){
        ((TextView) findViewById(R.id.output)).setText(test());
    }

    private String test() {
        return new String("JAVA VERSION");
    }

}
