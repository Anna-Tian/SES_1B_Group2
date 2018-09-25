package com.example.anna.ses_1b_group2.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.ses_1b_group2.R;
import com.example.anna.ses_1b_group2.utils.UniversalImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class SelectPhotoDialog extends DialogFragment {

    private static final String TAG = "SelectPhotoDialog";
    private static final int PICKFILE_REQUEST_CODE = 1234;
    private static final int CAMERA_REQUEST_CODE = 4321;

    public interface OnPhotoSelectedListener{
        void getImagePath(Uri imagePath);
        void getImageBitmap(Bitmap bitmap);
    }
    OnPhotoSelectedListener mOnPhotoSelectedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_selectphoto, container, false);

        TextView selectPhoto = (TextView) view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: accessing phones memory.");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        TextView takePhoto = (TextView) view.findViewById(R.id.dialogOpenCamera);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: starting camera.");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
            Results when selecting a new image from memory
         */
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult: image uri: " + selectedImageUri);

            //send the uri to PostFragment & dismiss dialog
            mOnPhotoSelectedListener.getImagePath(selectedImageUri);
            getDialog().dismiss();
        }
        /*
            Results when taking a new photo with camera
         */
        else if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking new photo");
            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");

            //send the bitmap to PostFragment and dismiss dialog
            mOnPhotoSelectedListener.getImageBitmap(bitmap);
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        try{
            mOnPhotoSelectedListener = (OnPhotoSelectedListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }

    /**
     * Created by User on 10/22/2017.
     */

    public static class PostFragment extends Fragment implements OnPhotoSelectedListener {

        private static final String TAG = "PostFragment";

        @Override
        public void getImagePath(Uri imagePath) {
            Log.d(TAG, "getImagePath: setting the image to imageview");
            UniversalImageLoader.setImage(imagePath.toString(), mPostImage);
            //assign to global variable
            mSelectedBitmap = null;
            mSelectedUri = imagePath;
        }

        @Override
        public void getImageBitmap(Bitmap bitmap) {
            Log.d(TAG, "getImageBitmap: setting the image to imageview");
            mPostImage.setImageBitmap(bitmap);
            //assign to a global variable
            mSelectedUri = null;
            mSelectedBitmap = bitmap;
        }

        //widgets
        private ImageView mPostImage;
        private EditText mTitle, mDescription, mPrice, mCountry, mStateProvince, mCity, mContactEmail;
        private Button mPost;
        private ProgressBar mProgressBar;

        //vars
        private Bitmap mSelectedBitmap;
        private Uri mSelectedUri;
        private byte[] mUploadBytes;


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_post, container, false);
            mPostImage = view.findViewById(R.id.post_image);
            mTitle = view.findViewById(R.id.input_title);
            mDescription = view.findViewById(R.id.input_description);
            mContactEmail = view.findViewById(R.id.input_email);
            mPost = view.findViewById(R.id.btn_post);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            init();

            return view;
        }

        private void init(){

            mPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: opening dialog to choose new photo");
                    SelectPhotoDialog dialog = new SelectPhotoDialog();
                    dialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                    dialog.setTargetFragment(PostFragment.this, 1);
                }
            });

            mPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: attempting to post...");
                    if(!isEmpty(mTitle.getText().toString())
                            && !isEmpty(mDescription.getText().toString())
                            && !isEmpty(mPrice.getText().toString())
                            && !isEmpty(mCountry.getText().toString())
                            && !isEmpty(mStateProvince.getText().toString())
                            && !isEmpty(mCity.getText().toString())
                            && !isEmpty(mContactEmail.getText().toString())){

                        //we have a bitmap and no Uri
                        if(mSelectedBitmap != null && mSelectedUri == null){
                            uploadNewPhoto(mSelectedBitmap);
                        }
                        //we have no bitmap and a uri
                        else if(mSelectedBitmap == null && mSelectedUri != null){
                            uploadNewPhoto(mSelectedUri);
                        }
                    }else{
                        Toast.makeText(getActivity(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void uploadNewPhoto(Bitmap bitmap){

        }

        private void uploadNewPhoto(Uri imagePath){

        }

        public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

            Bitmap mBitmap;

            public BackgroundImageResize(Bitmap bitmap) {
                if(bitmap != null){
                    this.mBitmap = bitmap;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(getActivity(), "compressing image", Toast.LENGTH_SHORT).show();
                showProgressBar();
            }

            @Override
            protected byte[] doInBackground(Uri... params) {
                Log.d(TAG, "doInBackground: started.");

                if(mBitmap == null){
                    try{
                        mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), params[0]);
                    }catch (IOException e){
                        Log.e(TAG, "doInBackground: IOException: " + e.getMessage());
                    }
                }
                byte[] bytes = null;
                bytes = getBytesFromBitmap(mBitmap, 100);
                return bytes;
            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);
                mUploadBytes = bytes;
                hideProgressBar();
                //execute the upload task

            }
        }

        public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
            return stream.toByteArray();
        }


        private void resetFields(){
            UniversalImageLoader.setImage("", mPostImage);
            mTitle.setText("");
            mDescription.setText("");
            mPrice.setText("");
            mCountry.setText("");
            mStateProvince.setText("");
            mCity.setText("");
            mContactEmail.setText("");
        }

        private void showProgressBar(){
            mProgressBar.setVisibility(View.VISIBLE);

        }

        private void hideProgressBar(){
            if(mProgressBar.getVisibility() == View.VISIBLE){
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * Return true if the @param is null
         * @param string
         * @return
         */
        private boolean isEmpty(String string){
            return string.equals("");
        }


    }
}











