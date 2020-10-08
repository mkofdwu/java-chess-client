package org.example.javachessclient.apis;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileApi {
    @POST("file")
    @Multipart
    Call<Void> uploadFile(@Part RequestBody file);
}
