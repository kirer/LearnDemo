package com.kirer.bindingadapter.image;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.kirer.bindingadapter.ReplyCommand;


/**
 * Created by kelin on 16-3-24.
 */
public final class ViewBindingAdapter {

//    @BindingAdapter({"uri"})
//    public static void setImageUri(ImageView simpleDraweeView, String uri) {
//        if (!TextUtils.isEmpty(uri)) {
//            simpleDraweeView.setImageURI(Uri.parse(uri));
//        }
//    }
//
//
//    @BindingAdapter(value = {"uri", "placeholderImageRes", "request_width", "request_height", "onSuccessCommand", "onFailureCommand"}, requireAll = false)
//    public static void loadImage(final ImageView imageView, String uri,
//                                 @DrawableRes int placeholderImageRes,
//                                 int width, int height,
//                                 final ReplyCommand<Bitmap> onSuccessCommand,
//                                 final ReplyCommand<DataSource<CloseableReference<CloseableImage>>> onFailureCommand) {
//        imageView.setImageResource(placeholderImageRes);
//        if (!TextUtils.isEmpty(uri)) {
//            ImagePipeline imagePipeline = Fresco.getImagePipeline();
//            ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
//            if (width > 0 && height > 0) {
//                builder.setResizeOptions(new ResizeOptions(width, height));
//            }
//            ImageRequest request = builder.build();
//            DataSource<CloseableReference<CloseableImage>>
//                    dataSource = imagePipeline.fetchDecodedImage(request, imageView.getContext());
//            dataSource.subscribe(new BaseBitmapDataSubscriber() {
//                @Override
//                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                    if (onFailureCommand != null) {
//                        onFailureCommand.execute(dataSource);
//                    }
//                }
//
//                @Override
//                protected void onNewResultImpl(Bitmap bitmap) {
//                    imageView.setImageBitmap(bitmap);
//                    if (onSuccessCommand != null) {
//                        onSuccessCommand.execute(bitmap);
//                    }
//                }
//            }, UiThreadImmediateExecutorService.getInstance());
//        }
//    }

}

