package com.project.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase myFirebaseDatabase;    //Declaring firebase database
    private DatabaseReference myDatabaseReference;  //Declaring database reference

    //Declarations
    public EditText editTitle;
    public EditText editPrice;
    public EditText editDescription;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        //Associating layout with activity
        editTitle = (EditText) findViewById(R.id.editText_title);
        editPrice = (EditText) findViewById(R.id.editText_price);
        editDescription = (EditText) findViewById(R.id.editText_description);

        //Retrieving intent files
        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null){
            deal = new TravelDeal();
        }
        this.deal = deal;
        editTitle.setText(deal.getTitle());
        editDescription.setText(deal.getDescription());
        editPrice.setText(deal.getPrice());

        FirebaseUtil.openFbReference("traveldeals");
        myFirebaseDatabase = FirebaseUtil.myFirebaseDatabase;
        myDatabaseReference = FirebaseUtil.myDatabaseReference;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ((item.getItemId())){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this,"Deal Saved", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_LONG).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void clean() {
        editTitle.setText("");
        editPrice.setText("");
        editDescription.setText("");
        editTitle.requestFocus();
    }

    private void saveDeal() {
        deal.setTitle(editTitle.getText().toString());
        deal.setDescription(editDescription.getText().toString());
        deal.setPrice(editPrice.getText().toString());
        if(deal.getId()==null){
            myDatabaseReference.push().setValue(deal);
        }
        else{
            myDatabaseReference.child(deal.getId()).setValue(deal);
        }
        //myDatabaseReference.push().setValue(deal); //pushing travel deal to my database
    }
    private void deleteDeal(){
        if(deal == null){
            Toast.makeText(this,"Please save the deal before deleting", Toast.LENGTH_SHORT).show();
            return;
        }
        myDatabaseReference.child(deal.getId()).removeValue();
    }

    private void backToList(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //used to create menu in the xml file
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

}
