package com.example.onlineclothingstore.AdminFragments.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Adapter.CategoryAdapter;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Constants.SwipeHelper;
import com.example.onlineclothingstore.EventBus.ToastEvent;
import com.example.onlineclothingstore.Model.CategoryModel;
import com.example.onlineclothingstore.R;
import com.example.onlineclothingstore.databinding.FragmentHomeBinding;
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
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    //Image upload
    private static final int PICK_IMAGE_REQUEST = 1234; //any number
    private ImageView img_category;
    //storage
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri = null;
    private List<CategoryModel> categoryModels;
    private CategoryAdapter categoryAdapter;
    private AlertDialog alertDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        //init view
        categoryView();

        //Observer pattern using ViewModel
        //viewModel
        homeViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        homeViewModel.getCategoryListMutable().observe(getViewLifecycleOwner(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModelList) {
                alertDialog.dismiss();
                categoryModels = categoryModelList;
                categoryAdapter = new CategoryAdapter(getContext(), categoryModels);
                binding.categoryRecycler.setAdapter(categoryAdapter);
            }
        });


        return binding.getRoot();
    }

    private void categoryView() {
        //init firebase storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        alertDialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        //setLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.categoryRecycler.setLayoutManager(layoutManager);
        binding.categoryRecycler.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

        //Init swipe helper options
        SwipeHelper swipeHelper = new SwipeHelper(getContext(), binding.categoryRecycler, 200) {
            @Override
            public void instantiateButton(RecyclerView.ViewHolder viewHolder, List<CustomButton> btn) {
                btn.add(new CustomButton(getContext(), "Remove",
                        30, 0, Color.parseColor("#FF3C30")//color
                        , pos -> {
                    Constants.categorySelected = categoryModels.get(pos);
                    initDeleteDialog();
                }));


            }
        };

        setHasOptionsMenu(true);
    }

    private void initDeleteDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Remove");
        builder.setMessage("Do you want to remove item from category?");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCategory();
            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
    //Remove Category
    private void deleteCategory() {
        FirebaseDatabase.getInstance()
                .getReference(Constants.CATEGORY_REFERENCE)
                .child(Constants.categorySelected.getCat_id())
                .removeValue()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                homeViewModel.loadCategories();
                EventBus.getDefault().postSticky(new ToastEvent(Constants.ACTION.DELETE, true));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_category) {
            createItemDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    // Builder pattern to create new Category
    private void createItemDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Create");
        builder.setMessage("Please fill information");

        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_category, null);

        EditText edt_catName = itemView.findViewById(R.id.edt_cat_name);
        img_category = itemView.findViewById(R.id.category_img);

        //set data
        Glide.with(getContext()).load(R.drawable.ic_baseline_image_24).into(img_category);

        //set Event
        img_category.setOnClickListener(new View.OnClickListener() {
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
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setName(edt_catName.getText().toString().trim());
                categoryModel.setStocks(new ArrayList<>()); // Create empty list for new food in category
                if (imageUri != null) {
                    //init firebase storage for image storage
                    alertDialog.setMessage("Uploading...");
                    alertDialog.show();

                    String unique_name = UUID.randomUUID().toString();
                    StorageReference imgFolder = storageReference.child("images/" + unique_name);
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
                                    categoryModel.setImage(uri.toString());
                                    createCategory(categoryModel);
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
                    createCategory(categoryModel);
                }

            }
        });

        //Show Dialog
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    //create category
    private void createCategory(CategoryModel categoryModel) {
        FirebaseDatabase.getInstance()
                .getReference(Constants.CATEGORY_REFERENCE)
                .push()
                .setValue(categoryModel)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                homeViewModel.loadCategories();
                EventBus.getDefault().postSticky(new ToastEvent(Constants.ACTION.CREATE, true));

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
                img_category.setImageURI(imageUri);
            }

        }
    }
}