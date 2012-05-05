package greendizer.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.greendizer.api.client.BuyerClient;
import com.greendizer.api.client.SellerClient;
import com.greendizer.api.resource.buyer.Buyer;
import com.greendizer.api.resource.seller.Seller;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        BuyerClient buyerClient = new BuyerClient(
        	    "gdoidbuyer@yopmail.com",
        	    "qwbdUJ19"
        	);
        Buyer buyer = buyerClient.getUser();
        buyer.refresh();
        
        SellerClient sellerClient = new SellerClient(
        	    "gdoidseller@yopmail.com",
        	    "qgNFcCs3"
        	);
        Seller seller = sellerClient.getUser();
        seller.refresh();
        
        
        TextView tv = new TextView(this);
        tv.setText(seller.toString());
        setContentView(tv);
        
    }
}