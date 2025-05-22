package com.tadneit.sale.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.storage.bucket}")
    private String storageBucket;

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream credentials = new ClassPathResource(firebaseConfigPath).getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(credentials))
                    .setStorageBucket(storageBucket)
                    .build();

            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public Storage storage() throws IOException {
        InputStream credentials = new ClassPathResource(firebaseConfigPath).getInputStream();

        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(credentials))
                .build()
                .getService();
    }
    @Bean
    public Firestore firestore() throws IOException {
        return FirestoreClient.getFirestore(firebaseApp());
    }

    @Bean
    public StorageClient storageClient() throws IOException {
        return StorageClient.getInstance(firebaseApp());
    }

}
