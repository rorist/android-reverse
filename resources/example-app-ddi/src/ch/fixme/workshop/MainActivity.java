package ch.fixme.workshop;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		test();
	}

	private void test() {
        ((TextView) findViewById(R.id.output)).setText("JAVA VERSION");
	}

}
