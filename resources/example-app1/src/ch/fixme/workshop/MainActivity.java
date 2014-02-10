package ch.fixme.workshop;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private boolean valid = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		test();
	}

	private void test() {
		if (valid) {
			((TextView) findViewById(R.id.output)).setText("CONGRATZ!");
		}
	}

}
