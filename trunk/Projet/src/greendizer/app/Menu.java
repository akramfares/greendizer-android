package greendizer.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager.LayoutParams;


public class Menu extends Activity{
	public static Context c;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
                LayoutParams.FLAG_FULLSCREEN);
	}
}
