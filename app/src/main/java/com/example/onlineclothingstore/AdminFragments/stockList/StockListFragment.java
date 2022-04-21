package com.example.onlineclothingstore.AdminFragments.stockList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Adapter.StocksAdapter;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Constants.SwipeHelper;
import com.example.onlineclothingstore.EventBus.ToastEvent;
import com.example.onlineclothingstore.Model.StockModel;
import com.example.onlineclothingstore.R;
import com.example.onlineclothingstore.databinding.FragmentStockListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class StockListFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1234;
    private StockListViewModel stockListViewModel;
    private FragmentStockListBinding binding;
    private List<StockModel> stockModels;
    private StocksAdapter stocksAdapter;
    private ImageView stock_image;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri = null;

    private android.app.AlertDialog alertDialog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStockListBinding.inflate(inflater, container, false);

        stockListViewModel = new ViewModelProvider(this).get(StockListViewModel.class);
        alertDialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //Init stocks
        stockViews();


        stockListViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<StockModel>>() {
            @Override
            public void onChanged(List<StockModel> stockModelList) {
                if (stockModelList != null) {
                    stockModels = stockModelList;
                    stocksAdapter = new StocksAdapter(getContext(), stockModels);
                    binding.stockListRecycler.setAdapter(stocksAdapter);
                }
            }
        });

        return binding.getRoot();
    }

    private void stockViews() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Constants.categorySelected.getName());
        binding.stockListRecycler.setHasFixedSize(true);
        binding.stockListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        setHasOptionsMenu(true);

        // Get Size of the button
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        SwipeHelper swipeHelper = new SwipeHelper(getContext(), binding.stockListRecycler, width/6) {
            @Override
            public void instantiateButton(RecyclerView.ViewHolder viewHolder, List<CustomButton> btn) {
                btn.add(new CustomButton(getContext(), "Remove",
                        30, 0, Color.parseColor("#9b0000")//color //#9b0000
                        , pos -> {
                    //Stopped HERE ***************
                    if (stockModels != null) {
                        Constants.selectedStock = stockModels.get(pos);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("REMOVE").setMessage("Do you want to remove this food?")
                                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        StockModel stockModel = stocksAdapter.getItemPosition(pos); //get item in adapter
                                        // check position if posInList != -1 (clicks from search view)
                                        if (stockModel.getPosInList() == -1) // If == -1 default, do nothing
                                            Constants.categorySelected.getStocks().remove(pos);
                                        else
                                            Constants.categorySelected.getStocks().remove(stockModel.getPosInList()); // remove by index was saved

                                        updateStock(Constants.categorySelected.getStocks(), Constants.ACTION.DELETE);
                                    }
                                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }));
                // Update
                btn.add(new CustomButton(getContext(), "Update",
                        30, 0, Color.parseColor("#560027")//color //#9b0000
                        , pos -> {
                    StockModel stockModel = stocksAdapter.getItemPosition(pos);
                    if (stockModel.getPosInList() == -1)
                        updateDialog(pos, stockModel);
                    else
                        updateDialog(stockModel.getPosInList(), stockModel);

                }));
            }
        };
    }

    private void updateDialog(int pos, StockModel stockModel) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Create");
        builder.setMessage("Please fill information");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_create_stock_item, null);
        EditText item_stock_name = itemView.findViewById(R.id.item_name);
        EditText item_stock_level = itemView.findViewById(R.id.item_stock);
        EditText item_stock_manufacturer = itemView.findViewById(R.id.item_manufacturer);
        EditText item_stock_category = itemView.findViewById(R.id.item_category);
        EditText item_stock_price = itemView.findViewById(R.id.item_price);
        stock_image = itemView.findViewById(R.id.item_img);

        //get Data
        item_stock_name.setText(new StringBuilder().append(stockModel.getName()));
        item_stock_level.setText(new StringBuilder().append(stockModel.getStockCount()));
        item_stock_manufacturer.setText(new StringBuilder().append(stockModel.getManufacturer()));
        item_stock_category.setText(new StringBuilder().append(stockModel.getCategory()));
        item_stock_price.setText(new StringBuilder().append(stockModel.getPrice()));
        Glide.with(getContext()).load(stockModel.getImage()).into(stock_image);

        //set Data
        stock_image.setOnClickListener(new View.OnClickListener() {
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
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StockModel updateStock = new StockModel();
                updateStock.setName(item_stock_name.getText().toString().trim());
                updateStock.setStockCount(Integer.parseInt(item_stock_level.getText().toString().trim()));
                updateStock.setManufacturer(item_stock_manufacturer.getText().toString().trim());
                updateStock.setCategory(item_stock_category.getText().toString().trim());
                updateStock.setPrice(Double.parseDouble(item_stock_price.getText().toString().trim()));

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
                                   updateStock.setImage(uri.toString());
                                   Constants.categorySelected.getStocks().set(pos, updateStock);
                                   updateStock(Constants.categorySelected.getStocks(), Constants.ACTION.UPDATE);
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            alertDialog.setMessage(new StringBuilder("Uploading: ").append(progress).append("%"));
                        }
                    });
                }else {
                    Constants.categorySelected.getStocks().set(pos, updateStock);
                    updateStock(Constants.categorySelected.getStocks(), Constants.ACTION.UPDATE);
                }
            }
        });

        // Show Dialog
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_stock)
            initCreateStockDialog();
        return super.onOptionsItemSelected(item);
    }

    //Create Stock
    private void initCreateStockDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Create");
        builder.setMessage("Please fill information");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_create_stock_item, null);
        EditText item_stock_name = itemView.findViewById(R.id.item_name);
        EditText item_stock_level = itemView.findViewById(R.id.item_stock);
        EditText item_stock_manufacturer = itemView.findViewById(R.id.item_manufacturer);
        EditText item_stock_category = itemView.findViewById(R.id.item_category);
        EditText item_stock_price = itemView.findViewById(R.id.item_price);
        stock_image = itemView.findViewById(R.id.item_img);

        //Set Data
        Glide.with(getContext()).load(R.drawable.ic_baseline_image_search_24).into(stock_image);
        stock_image.setOnClickListener(new View.OnClickListener() {
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
                stockModel.setStock_id(UUID.randomUUID().toString());
                stockModel.setName(item_stock_name.getText().toString().trim());
                stockModel.setStockCount(Integer.parseInt(item_stock_level.getText().toString().trim()));
                stockModel.setManufacturer(item_stock_manufacturer.getText().toString().trim());
                stockModel.setCategory(item_stock_category.getText().toString().trim());
                stockModel.setPrice(Double.parseDouble(item_stock_price.getText().toString().trim()));

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
                                    if (Constants.categorySelected.getStocks() == null) {
                                        Constants.categorySelected.setStocks(new ArrayList<>());
                                    }
                                    Constants.categorySelected.getStocks().add(stockModel);
                                    updateStock(Constants.categorySelected.getStocks(), Constants.ACTION.CREATE);
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            alertDialog.setMessage(new StringBuilder("Uploading: ").append(progress).append("%"));
                        }
                    });
                } else {
                    if (Constants.categorySelected.getStocks() == null){
                        Constants.categorySelected.setStocks(new ArrayList<>());
                    }
                    Constants.categorySelected.getStocks().add(stockModel);
                    updateStock(Constants.categorySelected.getStocks(), Constants.ACTION.CREATE);
                }
            }
        });

        // Show Dialog
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateStock(List<StockModel> stocks, Constants.ACTION action) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("stocks", stocks);

        FirebaseDatabase.getInstance()
                .getReference(Constants.CATEGORY_REFERENCE)
                .child(Constants.categorySelected.getCat_id())
                .updateChildren(updateData).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    stockListViewModel.getListMutableLiveData();
                    EventBus.getDefault().postSticky(new ToastEvent(action, true));
                }
            }
        });
    }

    //implement search here
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.stock_list_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        //Event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStock(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Clear text when clear button on search view
        ImageView clearBtn = searchView.findViewById(R.id.search_close_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = searchView.findViewById(R.id.search_src_text);
                //Clear text field
                editText.setText("");
                //Clear query
                searchView.setQuery("", false);
                //Collapse action view
                searchView.onActionViewCollapsed();
                //Collapse search widget
                menuItem.collapseActionView();
                //Restore recyclerView
                stockListViewModel.getListMutableLiveData().setValue(Constants.categorySelected.getStocks());
            }
        });

    }

    private void searchStock(String query) {
        List<StockModel> searchResults = new ArrayList<>();
        for (int i = 0; i< Constants.categorySelected.getStocks().size(); i++){
            StockModel stockModel = Constants.categorySelected.getStocks().get(i);
            if (stockModel.getName().toLowerCase().contains(query.toLowerCase()) || stockModel.getManufacturer().toLowerCase().contains(query.toLowerCase())){
                stockModel.setPosInList(i); // save index
                searchResults.add(stockModel);
            }
        }
        //set search results
        stockListViewModel.getListMutableLiveData().setValue(searchResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                stock_image.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}