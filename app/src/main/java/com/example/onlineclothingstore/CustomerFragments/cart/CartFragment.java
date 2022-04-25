package com.example.onlineclothingstore.CustomerFragments.cart;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlineclothingstore.Adapter.CartAdapter;
import com.example.onlineclothingstore.Callback.ICartLoadListener;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Constants.SwipeHelper;
import com.example.onlineclothingstore.Model.CartModel;
import com.example.onlineclothingstore.R;
import com.example.onlineclothingstore.databinding.FragmentCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment{

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;
    private CartAdapter cartAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding = FragmentCartBinding.inflate(inflater, container, false);
        
        initViews();

        mViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getCarListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CartModel>>() {
            @Override
            public void onChanged(List<CartModel> cartModelList) {
                if (cartModelList ==null || cartModelList.isEmpty()) {
                    binding.cartRcv.setVisibility(View.GONE);
                    binding.cartItemTotal.setText(new StringBuilder("Total: €0.0"));

                }else{
                    double sum = 0;
                    for (CartModel cartModel : cartModelList) {
                        sum += cartModel.getTotalPrice();
                    }
                    binding.cartItemTotal.setText(new StringBuilder("Total: €").append(sum));
                    cartAdapter = new CartAdapter(getContext(),cartModelList);
                    binding.cartRcv.setAdapter(cartAdapter);
                }


            }
        });

        return  binding.getRoot();
    }


    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.cartRcv.setLayoutManager(layoutManager);
        binding.cartRcv.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));

        //swipehelper
        SwipeHelper swipeHelper = new SwipeHelper(getContext(), binding.cartRcv, 200) {
            @Override
            public void instantiateButton(RecyclerView.ViewHolder viewHolder, List<CustomButton> btn) {
                btn.add(new CustomButton(getContext(), "Remove",
                        30, 0, Color.parseColor("#FF3C30")//color
                        , pos -> {
                    CartModel cartModel = cartAdapter.getItemPosition(pos);
                    initDeleteDialog(cartModel, pos);
                }));
                
            }
        };
        
    }

    private void initDeleteDialog(CartModel cartModel, int pos) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Remove");
        builder.setMessage("Do you want to remove item from cart?");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCartItem(cartModel ,pos);
            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCartItem(CartModel cartModel,int pos) {
        FirebaseDatabase.getInstance().getReference(Constants.CART_REF)
                .child(Constants.currentUser.getUid())
                .child(cartModel.getKey())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cartAdapter.notifyItemRemoved(pos);
                        mViewModel.loadCartFromDB();
                        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}