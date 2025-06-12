package com.example.lab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.lab5.adapter.DistributorAdapter;
import com.example.lab5.handle.Item_Distributor_handle;
import com.example.lab5.models.Distributor;
import com.example.lab5.models.Response;
import com.example.lab5.services.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DistributorAdapter distributorAdapter;
    private List<Distributor> distributorList;
    private HttpRequest httpRequest;
    private Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddUser = findViewById(R.id.btnAddUser);
        // Khởi tạo HttpRequest
        httpRequest = new HttpRequest();

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách user và adapter
        distributorList = new ArrayList<>();
        distributorAdapter = new DistributorAdapter(this, (ArrayList<Distributor>) distributorList, new Item_Distributor_handle() {
            @Override
            public void onEdit(int position) {
                Log.d("apiSua", "vi trí: " + position);
                showDialog(distributorList.get(position), position);

            }
            @Override
            public void onDelete(int position) {
                Log.d("apiXoa", "vị trí: " + position);
                deleteDistributor(distributorList.get(position).getId(), position);
            }
        });
        recyclerView.setAdapter(distributorAdapter);

        // Nút thêm user

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("apiThem", "ok them ");
                showDialog(null, -1);
            }
        });
        // Gọi API để lấy danh sách user
        fetchUserList();
    }

    private void fetchUserList() {
        // Gọi API bằng HttpRequest
        Call<Response<ArrayList<Distributor>>> call = httpRequest.callApi().getListDistributor();
        call.enqueue(new Callback<Response<ArrayList<Distributor>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Distributor>>> call,
                                   retrofit2.Response<Response<ArrayList<Distributor>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<ArrayList<Distributor>> apiResponse = response.body();

                    Log.d("API_RESPONSE", "dữ liệu: " + apiResponse.getData().toString());
                    if (apiResponse.getStatus() == 200) {
                        distributorList.clear();
                        distributorList.addAll(apiResponse.getData());
                        distributorAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Lỗi: " + apiResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(Distributor distributor, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_distributor, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText edDistributorName = dialogView.findViewById(R.id.edDistributor);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // dữ liệu nếu là sửa
        if (distributor != null) {
            edDistributorName.setText(distributor.getNameDistributor() != null ? distributor.getNameDistributor() : "");
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edDistributorName.getText().toString().trim();

                // đối tượng user
                Distributor newDistributor = new Distributor();
                newDistributor.setNameDistributor(username);

                if (distributor == null) {
                    // Thêm user mới
                    addUser(newDistributor);
                } else {
                    // Cập nhật user
                    newDistributor.setId(distributor.getId());
                    updateDistributor(newDistributor, position);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        Log.d("Dialog", "Dialog đã tạo");
    }
    private void addUser(Distributor distributor){
        Call<Response<Distributor>> call = httpRequest.callApi().addDistributor(distributor);
        call.enqueue(new Callback<Response<Distributor>>() {
            @Override
            public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<Distributor> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200) {
                        fetchUserList();
                        Toast.makeText(MainActivity.this, "thêm thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Lỗi: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi thêm user", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Response<Distributor>> call, Throwable t) {

            }
        });
    }
    private void updateDistributor(Distributor distributor, int position) {
        Call<Response<Distributor>> call = httpRequest.callApi().updateDistributor(distributor.getId(), distributor);
        call.enqueue(new Callback<Response<Distributor>>() {
            @Override
            public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Response<Distributor> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        // Tìm vị trí của item cũ và cập nhật nó
                        for (int i = 0; i < distributorList.size(); i++) {
                            if (distributorList.get(i).getId().equals(distributor.getId())) {
                                distributorList.set(i, apiResponse.getData());
                                distributorAdapter.notifyItemChanged(i);
                                break;
                            }
                        }
                        Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Cập nhật không thành công";
                        Log.e("UPDATE_API_ERROR", "Status: " + apiResponse.getStatus() + ", Message: " + apiResponse.getMessage() + ", Data: " + apiResponse.getData());
                        Toast.makeText(MainActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("UPDATE_REQUEST_ERROR", "Response not successful or body is null. Code: " + response.code() + " Message: " + response.message());
                    Toast.makeText(MainActivity.this, "Lỗi sửa dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<Distributor>> call, Throwable t) {
                Log.e("UPDATE_ERROR", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDistributor(String id, int position) {
        Call<Response<Distributor>> call = httpRequest.callApi().deleteDistributor(id);
        call.enqueue(new Callback<Response<Distributor>>() {
            @Override
            public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
                if (response.isSuccessful()) {
                    // Xóa item khỏi list và cập nhật UI ngay lập tức
                    distributorList.remove(position);
                    distributorAdapter.notifyItemRemoved(position);
                    Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<Distributor>> call, Throwable t) {
                Log.e("DELETE_ERROR", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
