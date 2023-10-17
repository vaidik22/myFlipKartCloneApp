package com.example.flipcartClone;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.flicpcartClone.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class EditProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private ImageView profileImageView;
    private ImageButton chooseImageButton;
    private Uri newImageUri;
    private EditText nameTextView1;
    private EditText usernameTextView1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__edit_profile, container, false);
        profileImageView = rootView.findViewById(R.id.profileImageView);
        chooseImageButton = rootView.findViewById(R.id.chooseImageButton);
        nameTextView1 = rootView.findViewById(R.id.edit_Text_Name);
        usernameTextView1 = rootView.findViewById(R.id.edit_Text_Username);
        SessionManager sessionManager = new SessionManager(requireContext());
        ((HomeActivity) requireActivity()).toggleBottomNavigationView(false);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the CAMERA permission is granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // The permission is already granted, proceed with opening the camera or gallery
                    openImagePicker();
                } else {
                    // Request the CAMERA permission
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });
        Button submitButton = rootView.findViewById(R.id.saveButton);
        submitButton.setOnClickListener(v -> {
            Log.d("DEBUG", "Submit button clicked");

            String newName = nameTextView1.getText().toString();
            String newUsername = usernameTextView1.getText().toString();

            // Convert the selected image to base64
            String newImageData = convertImageToBase64(profileImageView);

            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
            String userId = sessionManager.getPhoneNumber();
            dbHelper.updateUserInfo(userId, newName, newUsername, newImageData);
            Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show();
        });

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        String userId = sessionManager.getPhoneNumber();
        Cursor cursor = dbHelper.getUsernameAndName(userId);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
            EditText nameTextView = rootView.findViewById(R.id.edit_Text_Name);
            EditText usernameTextView = rootView.findViewById(R.id.edit_Text_Username);
            nameTextView.setText(name); // Set the retrieved name to the TextView
            usernameTextView.setText(username); // Set the retrieved username to the TextView
            if (image != null) {
                Log.e("image_path", image);
                profileImageView.setImageURI(Uri.parse(image));
            }
            if (image != null) {
                Log.e("image_path", image);
                byte[] decodedImageBytes = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedImageBitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
                profileImageView.setImageBitmap(decodedImageBitmap);
            }
            cursor.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                newImageUri = data.getData();
            }

            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(newImageUri);
                Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                // Compress the bitmap
                int maxWidth = 800;
                int maxHeight = 800;
                float scale = Math.min(((float) maxWidth / originalBitmap.getWidth()), ((float) maxHeight / originalBitmap.getHeight()));

                int newWidth = Math.round(originalBitmap.getWidth() * scale);
                int newHeight = Math.round(originalBitmap.getHeight() * scale);

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                profileImageView.setImageBitmap(resizedBitmap);

                String newName = nameTextView1.getText().toString();
                String newUsername = usernameTextView1.getText().toString();
                DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
                SessionManager sessionManager = new SessionManager(requireContext());
                String userId = sessionManager.getPhoneNumber();
                dbHelper.updateUserInfo(userId, newName, newUsername, base64Image);

                Log.e("base64Image", base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {

            if (data != null) {
                newImageUri = data.getData();
            }

            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(newImageUri);
                Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                // Compress the bitmap
                int maxWidth = 800;
                int maxHeight = 800;
                float scale = Math.min(((float) maxWidth / originalBitmap.getWidth()), ((float) maxHeight / originalBitmap.getHeight()));

                int newWidth = Math.round(originalBitmap.getWidth() * scale);
                int newHeight = Math.round(originalBitmap.getHeight() * scale);

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                profileImageView.setImageBitmap(resizedBitmap);

                String newName = nameTextView1.getText().toString();
                String newUsername = usernameTextView1.getText().toString();
                DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
                SessionManager sessionManager = new SessionManager(requireContext());
                String userId = sessionManager.getPhoneNumber();
                dbHelper.updateUserInfo(userId, newName, newUsername, base64Image);

                Log.e("base64Image", base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String convertImageToBase64(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Compress the bitmap
        int maxWidth = 800;
        int maxHeight = 800;
        float scale = Math.min(((float) maxWidth / bitmap.getWidth()), ((float) maxHeight / bitmap.getHeight()));

        int newWidth = Math.round(bitmap.getWidth() * scale);
        int newHeight = Math.round(bitmap.getHeight() * scale);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream); // Adjust compression quality as needed
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The CAMERA permission is granted, proceed with opening the camera or gallery
                openImagePicker();
            } else {

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity) requireActivity()).toggleBottomNavigationView(true);
    }

    private void openImagePicker() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
        } else {
            // If no camera app is available, show the gallery picker
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        }
    }

}