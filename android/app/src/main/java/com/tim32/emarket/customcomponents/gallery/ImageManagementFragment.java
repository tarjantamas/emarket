package com.tim32.emarket.customcomponents.gallery;

import android.content.*;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.MainActivity;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.ImageListResponse;
import com.tim32.emarket.apiclients.dto.ImageResponse;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.service.ImageService;
import com.tim32.emarket.util.BundleUtil;
import com.tim32.emarket.util.ImageUtil;
import org.androidannotations.annotations.*;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_image_management)
public class ImageManagementFragment extends Fragment {

    public static final int PICK_IMAGE_FROM_GALLERY = 1;
    public static final int TAKE_PHOTO = 2;
    private static final int ADD_OPERATION = 1;
    private static final int DELETE_OPERATION = 2;
    private static final int ADD_PROFILE_OPERATION = 3;

    public static Bitmap photo = null;
    public static Uri uri = null;
    public static List<String> filePaths = null;
    public static String filePath = null;

    @ViewById(R.id.imageGridView)
    GridView gridView;

    @ViewById(R.id.profileImage)
    ImageView profileImage;

    @Bean
    ImageService imageService;

    @Bean
    AuthService authService;

    List<String> imagePaths = new ArrayList<>();

    private GalleryAdapter galleryAdapter;

    @AfterViews
    void afterViews() {
        galleryAdapter = new GalleryAdapter(getContext(), imagePaths);
        loadImages();
    }

    @Click(R.id.addImageButton)
    void addImageButtonClicked() {
        selectImage(getContext());
    }

    @Background
    void loadImages() {
        Long companyId = BundleUtil.getLongOrNull(getArguments(), "company_id");
        Long shopId = BundleUtil.getLongOrNull(getArguments(), "shop_id");
        Long productId = BundleUtil.getLongOrNull(getArguments(), "product_id");

        ImageListResponse response = null;
        try {
            if (companyId != null) {
                response = imageService.getCompanyImages(companyId);
            } else if (shopId != null) {
                response = imageService.getShopImages(shopId);
            } else if (productId != null) {
                response = imageService.getProductImages(productId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response == null) {
            return;
        }
        imagePaths = new ArrayList<>();
        for (ImageResponse imageResponse : response) {
            imagePaths.add(RestApiUrl.root + RestApiUrl.image + imageResponse.getId());
        }
        setGridView();
    }

    @UiThread
    void setGridView() {
        gridView.setAdapter(galleryAdapter);
        gridView.setNestedScrollingEnabled(true);
        galleryAdapter.setImageUrls(imagePaths);
    }

    void uploadImage(MultiValueMap<String, Object> requestImage) {
        imagePaths = new ArrayList<>();

        if (getArguments().getLong("company_id") != 0) {
            try {
                ImageResponse response = imageService.addCompanyImage(getArguments().getLong("company_id"), requestImage);
                String imagePath = RestApiUrl.root + "/api/v1/image/" + response.getId();
                addItemToGalleryAdapter(imagePath);
                makeSuccessToast();
            } catch (Exception e) {
                makeFailToast();
            }
        } else if (getArguments().getLong("shop_id") != 0) {
            try {
                ImageResponse response = imageService.addShopImage(getArguments().getLong("shop_id"), requestImage);
                String imagePath = RestApiUrl.root + "/api/v1/image/" + response.getId();
                addItemToGalleryAdapter(imagePath);
                makeSuccessToast();
            } catch (Exception e) {
                makeFailToast();
            }

        } else if (getArguments().getLong("product_id") != 0) {
            try {
                ImageResponse response = imageService.addProductImage(getArguments().getLong("product_id"), requestImage);
                String imagePath = RestApiUrl.root + "/api/v1/image/" + response.getId();
                addItemToGalleryAdapter(imagePath);
                makeSuccessToast();
            } catch (Exception e) {
                makeFailToast();
            }
        } else {
            try {
                imageService.addProfileImage(requestImage);
                updateProfileImage();
                makeSuccessToast();
            } catch (Exception e) {
                makeFailToast();
            }
        }
    }

    @Background
    void callDeleteImageService(String id) {
        try {
            imageService.deleteImage(Long.parseLong(id));
            makeDeleteSuccessToast();
        } catch (Exception e) {
            makeDeleteFailToast();
        }
    }

    @UiThread
    void makeDeleteFailToast() {
        Toast.makeText(getContext(), "Photo delete failed", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void makeDeleteSuccessToast() {
        Toast.makeText(getContext(), "Photo deleted successfully", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void addItemToGalleryAdapter(String imagePath) {
        galleryAdapter.addItem(imagePath);
    }

    @UiThread
    void deleteItemFromGalleryAdapter(String imagePath) {
        galleryAdapter.deleteItem(imagePath);
    }

    @UiThread
    void updateProfileImage() {
        MainActivity mainActivity = ((MainActivity) getActivity());
        if (mainActivity == null) {
            return;
        }
        mainActivity.updateProfileImage();
    }

    @UiThread
    void makeSuccessToast() {
        Toast.makeText(getContext(), "Photo uploaded successfully", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void makeFailToast() {
        Toast.makeText(getContext(), "Photo upload failed", Toast.LENGTH_SHORT).show();
    }

    @OnActivityResult(PICK_IMAGE_FROM_GALLERY)
    public void onPickImageFromGalleryResult(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        filePaths = new ArrayList<>();
        try {
            if (data.getClipData() == null) {
                loadImagesAndUploadForSamsungGallery(data.getData());
            } else {
                loadImagesAndUpload(data.getClipData());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Background
    void loadImagesAndUpload(ClipData mClipData) {
        if (mClipData == null) {
            return;
        }
        for (int i = 0; i < mClipData.getItemCount(); i++) {
            try {
                photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mClipData.getItemAt(i).getUri());
                filePath = saveToInternalStorage(photo, i + 1);
                filePaths.add(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        uploadImages();
        //updateGalleryAdapter(ADD_OPERATION);
    }

    @Background
    void loadImagesAndUploadForSamsungGallery(Uri uri) {
        if (uri == null) {
            return;
        }

        try {
            photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            filePath = saveToInternalStorage(photo, 1);
            filePaths.add(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        uploadImages();
        //updateGalleryAdapter(ADD_OPERATION);
    }

    @OnActivityResult(TAKE_PHOTO)
    void onTakePhotoResult(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        filePaths = new ArrayList<>();
        try {
            photo = (Bitmap) data.getExtras().get("data");
            filePath = saveToInternalStorage(photo, 1);
            filePaths.add(filePath);
            uploadImages();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Background
    void uploadImages() {
        for (String filePath : filePaths) {
            MultiValueMap<String, Object> requestImage = ImageUtil.wrapImageInMultiValueMap(filePath);
            uploadImage(requestImage);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, int imageCount) {
        ContextWrapper cw = new ContextWrapper(getContext());

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        File mypath = new File(directory, "photo" + imageCount);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose photo to upload");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_PHOTO);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(pickPhoto, PICK_IMAGE_FROM_GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @ItemLongClick(R.id.imageGridView)
    void listViewItemClick(int position) {
        final CharSequence[] options = {"Delete", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose photo to upload");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Delete")) {
                    String[] images = new String[gridView.getCount()];
                    for (int i = 0; i < gridView.getCount(); i++) {
                        images[i] = (String) gridView.getItemAtPosition(i);
                    }

                    filePath = (String) gridView.getItemAtPosition(position);
                    String[] split = filePath.split("/");
                    callDeleteImageService(split[split.length - 1]);
                    deleteItemFromGalleryAdapter(filePath);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
