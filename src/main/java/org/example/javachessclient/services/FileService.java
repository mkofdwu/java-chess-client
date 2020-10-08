package org.example.javachessclient.services;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.example.javachessclient.Store;
import org.example.javachessclient.models.UploadedFileUrl;

import java.io.File;
import java.io.IOException;

public class FileService {
    public static UploadedFileUrl uploadImage(File file) {
        try {
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            return Store.fileApi.uploadFile(filePart).execute().body();
        } catch (IOException exception) {
            System.out.println("Failed to upload file to server");
            exception.printStackTrace();
            return null;
        }
    }
}
