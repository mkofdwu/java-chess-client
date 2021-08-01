package org.example.javachessclient.services;

import org.example.javachessclient.Store;
import org.example.javachessclient.models.LoginDetails;
import org.example.javachessclient.models.RegisterDetails;
import org.example.javachessclient.models.TokenAuthResponse;
import org.example.javachessclient.models.User;
import retrofit2.Response;

import java.io.*;

public class AuthService {
    // currently only supports windows else linux
    private final static String dataDir = System.getProperty("os.name").equals("Windows")
            ? System.getenv("APPDATA") + "/JavaChess"
            : System.getProperty("user.home") + "/.JavaChess";
    private static final String jwtFilePath = dataDir + "/token.txt"; // NOTE: stores the jwt unencrypted

    public static boolean attemptAuthenticateFromFile() {
        // attempt to authenticate using the jwt stored in `jwtFilePath`
        try {
            BufferedReader br = new BufferedReader(new FileReader(jwtFilePath));
            String token = br.readLine();
            br.close();
            Store.token = token;
            Response<User> response = Store.authApi.getUser().execute();
            if (response.body() == null) {
                Store.token = null;
                return false;
            }
            Store.user = response.body();
            Store.stompSession = SocketGameService.createStompSession();
            return true;
        } catch (IOException exception) {
            System.out.println("Failed to authenticate from jwt file: " + exception.getMessage());
            return false;
        }
    }

    public static String authenticate(String username, String password, boolean register) {
        try {
            Response<TokenAuthResponse> response = (
                    register
                            ? Store.authApi.register(new RegisterDetails(username, password))
                            : Store.authApi.login(new LoginDetails(username, password))
            ).execute();
            if (response.body() == null) {
                System.out.println("Failed to authenticate: " + response.errorBody().string());
                return response.message();
            }

            TokenAuthResponse authResponse = response.body();
            Store.token = authResponse.getToken();
            Store.user = authResponse.getUser();
            Store.stompSession = SocketGameService.createStompSession();
            // save token to file
            File parent = new File(jwtFilePath).getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                return "Failed to create jwt file at " + jwtFilePath;
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(jwtFilePath));
            bw.write(authResponse.getToken());
            bw.close();
            return null;
        } catch (IOException exception) {
            return exception.getMessage();
        }
    }

    public static boolean logout() {
        // delete jwt file
        Store.token = null;
        return new File(jwtFilePath).delete();
    }
}
