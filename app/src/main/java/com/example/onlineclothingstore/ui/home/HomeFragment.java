package com.example.onlineclothingstore.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Adapter.StocksAdapter;
import com.example.onlineclothingstore.Model.StockModel;
import com.example.onlineclothingstore.R;
import com.example.onlineclothingstore.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    //Image upload
    private static final int PICK_IMAGE_REQUEST = 1234; //any number
    private ImageView img_item;
    //storage
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri = null;
    private List<StockModel> stockModel;
    private StocksAdapter stocksAdapter;
    AlertDialog alertDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        //init firebase storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        alertDialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        //Observer pattern using ViewModel
        setHasOptionsMenu(true);

        //setLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.stockRecycler.setLayoutManager(layoutManager);
        binding.stockRecycler.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

        //viewModel
        homeViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        homeViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<StockModel>>() {
            @Override
            public void onChanged(List<StockModel> stockModelList) {
                alertDialog.dismiss();
                stockModel = stockModelList;
                stocksAdapter = new StocksAdapter(getContext(), stockModel);
                binding.stockRecycler.setAdapter(stocksAdapter);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_item) {
            createItemDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createItemDialog() {
        //init Builder creational design pattern using AlertDialog.Builder class.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Create");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_create_stock_item, null);
        EditText edt_item_name = itemView.findViewById(R.id.item_name);
        EditText edt_item_stock = itemView.findViewById(R.id.item_stock);
        EditText edt_item_make = itemView.findViewById(R.id.item_manufacturer);
        EditText edt_item_category = itemView.findViewById(R.id.item_category);
        EditText edt_item_price = itemView.findViewById(R.id.item_price);
        img_item = itemView.findViewById(R.id.item_img);


        Glide.with(getContext()).load(R.drawable.ic_baseline_image_search_24).into(img_item);

        img_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StockModel stockModel = new StockModel();
                stockModel.setName(edt_item_name.getText().toString().trim());
                stockModel.setStockCount(Integer.parseInt(edt_item_stock.getText().toString().trim()));
                stockModel.setManufacturer(edt_item_make.getText().toString().trim());
                stockModel.setCategory(edt_item_category.getText().toString().trim());
                stockModel.setPrice(Double.parseDouble(edt_item_price.getText().toString().trim()));
                if (imageUri != null) {
                    //init firebase storage for image storage
                    alertDialog.show();
                    String unique_name = UUID.randomUUID().toString();
                    StorageReference imgFolder = storageReference.child("images/" + unique_name);
                    //set img to db
                    imgFolder.putFile(imageUri).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alertDialog.dismiss();
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            alertDialog.dismiss();
                            imgFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    stockModel.setImage(uri.toString());
                                    createStock(stockModel);
                                }
                            });
                        }
                    });
                } else {
                    createStock(stockModel);
                }
            }
        });

        builder.setCancelable(false);
        //show dialog builder
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void createStock(StockModel stockModel) {
        FirebaseDatabase.getInstance().getReference("Stocks")
                .push()
                .setValue(stockModel)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                homeViewModel.loadStocks();
                Toast.makeText(getContext(), "Created!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                img_item.setImageURI(imageUri);
            }

        }
    }
}