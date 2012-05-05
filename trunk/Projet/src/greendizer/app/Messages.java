package greendizer.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.greendizer.api.client.BuyerClient;
import com.greendizer.api.dal.Collection;
import com.greendizer.api.resource.buyer.Buyer;
import com.greendizer.api.resource.buyer.Email;
import com.greendizer.api.resource.buyer.Invoice;
import com.greendizer.api.resource.buyer.Message;



public class Messages extends Activity{
	public static Context c;
	private Buyer buyer;
	private String email;
	private String password;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	c = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices);
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
		
		final ProgressDialog dialog = ProgressDialog.show(Messages.this,"Please wait","Loading...",true);
        new Thread() 
        {
              public void run() 
              {
                   try 
                   {
                       Thread.sleep(2000);
                       buyer = buyerClient.getUser();
                       buyer.refresh();
                       setupTable();
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
       
       
        
        
	}

    private void setupTable() {
    	((Activity) c).runOnUiThread(new Runnable() {
			public void run() {
		    	TableLayout tl = (TableLayout)findViewById(R.id.myTableLayout);
		        tl.removeAllViews();
		        int i=0;
		        for(final com.greendizer.api.resource.buyer.Thread thread : getAllMessages()){
		        	
		    	 /* Create a new row to be added. */
		          TableRow tr = new TableRow(c);
		          tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		          /* Create a Text to be the row-content. */
		          TextView tv = new TextView(c);
		          tv.setText(thread.getSubject());
		          tv.setTextSize(20);
		          tv.setPadding(20, 0, 0, 0);
		          TextView sc = new TextView(c);
		          sc.setText(""+thread.getMessages().size());
		          sc.setTextSize(20);
		          sc.setGravity(Gravity.RIGHT);
		          sc.setPadding(0, 0, 20, 0);
		          sc.setTextColor(Color.YELLOW);
		          /* Add Text to row. */
		          if(i%2 == 0)tr.setBackgroundColor(Color.GRAY);
		          tr.addView(tv);
		          tr.addView(sc);
		          tr.setOnClickListener(new OnClickListener() {
		        	  @Override
		  			public void onClick(View v) {
		        		  Dialog namedialog = new Dialog(c);
		                  namedialog.setContentView(R.layout.maindialog);
		                  namedialog.setTitle(thread.getSubject());
		                  TextView tv = new TextView(c);
		                  String messages ="";
		                  for(final Message message : thread.getMessages()){
		                	  messages += message.getText();
		                	  messages +="\n ----------------------\n";
		                  }
		                  tv.setText("Messages : \n"+messages);
		                  namedialog.setContentView(tv);
		                  namedialog.show();
		        	  }
		          });
		          
		          /* Add row to TableLayout. */
		          tl.addView(tr,new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		          i++;
		        }
			}
    	});
	}

	public Collection<com.greendizer.api.resource.buyer.Thread> getAllMessages() {
		Email mail = buyer.getEmails().getById(email);
		Collection<com.greendizer.api.resource.buyer.Thread> collection = mail.getThreads().all();
		collection.populate();
		
		return collection;
		}
	
}
