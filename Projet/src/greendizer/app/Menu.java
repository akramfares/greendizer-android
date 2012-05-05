package greendizer.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greendizer.api.client.BuyerClient;
import com.greendizer.api.dal.Collection;
import com.greendizer.api.resource.buyer.Buyer;
import com.greendizer.api.resource.buyer.Email;
import com.greendizer.api.resource.buyer.Invoice;


public class Menu extends Activity{
	public static Context c;
	private Buyer buyer;
	private String email;
	private String password;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	c = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        setRequestedOrientation(1);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
                LayoutParams.FLAG_FULLSCREEN);
        
        Bundle bundle = this.getIntent().getExtras();
		email = bundle.getString("email");
		password = bundle.getString("password");
		
		final BuyerClient buyerClient = new BuyerClient(
        	    email,
        	    password
        	);
		
		final ProgressDialog dialog = ProgressDialog.show(Menu.this,"Please wait","Loading...",true);
        new Thread() 
        {
              public void run() 
              {
                   try 
                   {
                       Thread.sleep(2000);
                       buyer = buyerClient.getUser();
                       buyer.refresh();
                       setupInvoices();
                       setupMessages();
                   } catch (InterruptedException e) 
                   {
                       e.toString();
                       e.printStackTrace();
                   }
                   finally
                   {
                	   if (dialog.isShowing()) {
                           dialog.dismiss();
                       }       
                   }
              }
        }.start();
		
		
        
        
        setupDirectory();
        setupStatistics();
        
	}

	private void setupStatistics() {
		// TODO Auto-generated method stub
		
	}

	private void setupDirectory() {
		// TODO Auto-generated method stub
		
	}

	private void setupMessages() {
		((Activity) c).runOnUiThread(new Runnable() {
			public void run() {
		        final TextView tvInvoices = (TextView) findViewById(R.id.menuMessages);
				tvInvoices.setText("Messages");
				final Intent Intent = new Intent().setClass(c, Messages.class);
				ImageButton button = (ImageButton)findViewById(R.id.buttonMessages);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					        Bundle bundle = new Bundle();
					        bundle.putString("email", email);
					        bundle.putString("password", password);
					        Intent.putExtras(bundle);
					        startActivityForResult(Intent, 0);
					        //startActivity(Intent);
						
					}
		        });
			}
	});
		
	}

	private void setupInvoices() {
		((Activity) c).runOnUiThread(new Runnable() {
				public void run() {
			        final TextView tvInvoices = (TextView) findViewById(R.id.menuInvoices);
					tvInvoices.setText("Invoices ("+getAllInvoices().size()+")");
					final Intent Intent = new Intent().setClass(c, Invoices.class);
					ImageButton button = (ImageButton)findViewById(R.id.buttonInvoices);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
						        Bundle bundle = new Bundle();
						        bundle.putString("email", email);
						        bundle.putString("password", password);
						        Intent.putExtras(bundle);
						        startActivityForResult(Intent, 0);
						        //startActivity(Intent);
							
						}
			        });
				}
		});
	}
	
	public Collection<Invoice> getAllInvoices() {
		Email mail = buyer.getEmails().getById(email);
		Collection<Invoice> collection = mail.getInvoices().all();
		collection.populate();
		
		return collection;
		}
	
}
