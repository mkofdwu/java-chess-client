package org.example.javachessclient.apis;

import okhttp3.MultipartBody;
import org.example.javachessclient.models.UploadedFileUrl;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileApi {
    @Multipart
    @POST("file")
    Call<UploadedFileUrl> uploadFile(@Part MultipartBody.Part filePart);
}
