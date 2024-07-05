package com.example.datatransferproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datatransferproject.model.Datum;
import com.example.datatransferproject.model.Root;
import com.example.datatransferproject.model.User;
import com.example.datatransferproject.retrofit.APIClient;
import com.example.datatransferproject.retrofit.APIInteface;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView jsonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jsonTextView = findViewById(R.id.jsonTextView);
        fetchDataFromAPI();
    }

    private void fetchDataFromAPI() {

        APIInteface apiInteface = APIClient.getClient().create(APIInteface.class);
        apiInteface.getAllUsers().enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    String rawJson = new Gson().toJson(response.body());
                    Log.d(TAG, "Raw API Response: " + rawJson); // Log the raw response

                    if (root != null) {
                        ArrayList<Datum> users = root.getData();
//                        saveDataToJsonFile(users);
//                        displayJsonString();

                        if (users != null) {

                            if (!users.isEmpty()) {
                                Datum firstDatum = users.get(0);
                                firstDatum.setPhoneNumber("999999999");
                                firstDatum.setDob("1998-09-01");
                            }
                            Root newRoot = new Root();
                            saveDataToJsonFile(users);
                            displayJsonString();
                        } else {
                            Log.e(TAG, "Users list is null");
                        }
                    } else {
                        Log.e(TAG, "Root is null");
                    }
                } else {
                    Log.e(TAG, "API Response not successful");
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.e(TAG, "API Call failed: ", t);
            }
        });
//        APIInteface userService = APIClient.getUserService();
//        Call<Root> call = userService.getAllUsers();
//        call.enqueue(new Callback<Root>() {
//            @Override
//            public void onResponse(Call<Root> call, Response<Root> response) {
//                if (response.isSuccessful()) {
//                    Root root = response.body();
//                    if (root != null) {
//                        List<User> users = root.getUsers();
////                        saveDataToJsonFile(users);
////                        displayJsonString();
//
//                        if (users != null) {
//                            saveDataToJsonFile(users);
//                            displayJsonString();
//                        } else {
//                            Log.e(TAG, "Users list is null");
//                        }
//                    } else {
//                        Log.e(TAG, "Root is null");
//                    }
//                } else {
//                    Log.e(TAG, "API Response not successful");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Root> call, Throwable t) {
//                // Handle failure
//                Log.e(TAG, "API Call failed: ", t);
//                Toast.makeText(MainActivity.this, "ERROORR" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void saveDataToJsonFile(ArrayList<Datum> users) {
        // Save the data to a JSON file
        JSONHelper.saveJSON(this, users, "users.json");
    }

    private void displayJsonString() {
        String jsonString = JSONHelper.loadJSONString(this, "users.json");
        jsonTextView.setText(jsonString);
    }
}