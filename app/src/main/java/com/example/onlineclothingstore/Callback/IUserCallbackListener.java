package com.example.onlineclothingstore.Callback;


import com.example.onlineclothingstore.Model.UserModel;

import java.util.List;

public interface IUserCallbackListener {
    void onUserLoadSuccess(List<UserModel> userModelList);

    void onUserLoadFailed(String message);
}
