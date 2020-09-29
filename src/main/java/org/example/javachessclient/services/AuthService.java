package org.example.javachessclient.services;

import org.example.javachessclient.Store;
import org.example.javachessclient.apis.AuthApi;
import org.example.javachessclient.models.LoginDetails;
import org.example.javachessclient.models.RegisterDetails;
import org.example.javachessclient.models.TokenAuthResponse;
import org.example.javachessclient.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;

public class AuthService {
    // currently only supports windows else linux
    private final static String dataDir = System.getProperty("os.name").equals("Windows")
            ? System.getenv("APPDATA") + "/JavaChess"
            : System.getProperty("user.home") + "/.JavaChess";
    private static final String jwtFilePath = dataDir + "/token.txt"; // NOTE: stores the jwt unencrypted

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8081/api/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final AuthApi authApi = retrofit.create(AuthApi.class);

    public static boolean attemptAuthenticateFromFile() {
        // attempt to authenticate using the jwt stored in `jwtFilePath`
        try {
            BufferedReader br = new BufferedReader(new FileReader(jwtFilePath));
            String token = br.readLine();
            br.close();
            Response<User> response = authApi.getUser("Bearer " + token).execute();
            if (response.body() == null) {
                return false;
            }
            Store.user = response.body();
            return true;
        } catch (IOException exception) {
            System.out.println("Failed to authenticate from jwt file: " + exception.getMessage());
            return false;
        }
    }

    public static boolean authenticate(String username, String password, boolean register) {
        try {
            Response<TokenAuthResponse> response = (
                    register
                            ? authApi.register(new RegisterDetails(username, password))
                            : authApi.login(new LoginDetails(username, password))
            ).execute();
            if (response.body() == null) {
                return false;
            }

            TokenAuthResponse authResponse = response.body();
            // save token to file
            File parent = new File(jwtFilePath).getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IOException("Failed to create jwt file at " + jwtFilePath);
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(jwtFilePath));
            bw.write(authResponse.getToken());
            bw.close();
            // update Store.user
            Store.user = authResponse.getUser();
            return true;
        } catch (IOException exception) {
            System.out.println("Failed to authenticate or write token to file: " + exception.getMessage());
            return false;
        }
    }
}
