package greendizer.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greendizer.api.client.BuyerClient;
import com.greendizer.api.resource.buyer.Buyer;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        connection();
    }
    
    private void connection() {
		Button button = (Button)findViewById(R.id.LoginButton);
		final EditText email = (EditText) findViewById(R.id.Email);
		email.setText("gdoidbuyer@yopmail.com");
		final EditText password = (EditText) findViewById(R.id.Password);
		password.setText("qwbdUJ19");
		final Context c = this;
		final Intent Intent = new Intent().setClass(this, Menu.class);
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
					BuyerClient buyerClient = new BuyerClient(
			        	    email.getText().toString(),
			        	    password.getText().toString()
			        	);
			        Buyer buyer = buyerClient.getUser();
			        buyer.refresh();
			        startActivity(Intent);
				
			}
        });
	}
}