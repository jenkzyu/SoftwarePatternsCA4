package com.example.onlineclothingstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.CartModel;
import com.example.onlineclothingstore.Model.StockModel;
import com.example.onlineclothingstore.databinding.ActivityCheckoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private String totalAmount = "";
    private List<CartModel> cartModelList;
    private List<StockModel> stockModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        totalAmount = getIntent().getStringExtra("Total Price");
        cartModelList = getIntent().getParcelableArrayListExtra("CartItems");

        //set data
        binding.edtName.setText(Constants.currentUser.getName());
        binding.edtEmail.setText(Constants.currentUser.getEmail());
        binding.edtAddress.setText(Constants.currentUser.getAddress());

        binding.btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder();
            }
        });

    }

    private void confirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        DatabaseReference ordersRef = FirebaseDatabase.getInstance()
                .getReference(Constants.ORDER_REF)
                .child(Constants.currentUser.getUid());

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("name", binding.edtName.getText().toString());
        orderData.put("address", binding.edtAddress.getText().toString());
        orderData.put("email", binding.edtEmail.getText().toString());
        orderData.put("totalPayment", totalAmount);
        orderData.put("date", saveCurrentDate);
        orderData.put("time", saveCurrentTime);
        orderData.put("cartList", cartModelList);

        ordersRef.updateChildren(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference(Constants.CART_REF)
                            .child(Constants.currentUser.getUid())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CheckoutActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CheckoutActivity.this, CustomerHomeActivity.class));
                                        finish();

                                    }
                                }
                            });
                }

            }
        });

    }

}

