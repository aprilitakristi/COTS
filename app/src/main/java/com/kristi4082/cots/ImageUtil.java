package com.kristi4082.cots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Aprilita on 12/6/2016.
 */

public class ImageUtil {
        private Context context;

        public ImageUtil(Context context) {
            this.context = context;
        }

        public static ImageComposser with(Context context) {
            return new ImageComposser(context);
        }

        public static class ImageComposser {
            private Context context;
            private String url;
            private String name;

            private ImageComposser(Context context) {
                this.context = context;
            }

            public ImageComposser from(Animal animal) {
                this.url = animal.getImageUrl();
                this.name = animal.getName() + ".png";
                return this;
            }

            public void into(final ImageView imageView) {
                Bitmap bitmap = StorageUtil.with(context).getBitmap(name);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    return;
                }

                ImageDownloader downloader = new ImageDownloader(url, new OnImageLoadFinish() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError() {

                    }
                });
                downloader.execute();
            }

            public void into(final ImageView imageView, final OnImageLoadFinish onImageLoadFinish) {
                Bitmap bitmap = StorageUtil.with(context).getBitmap(name);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    onImageLoadFinish.onSuccess(bitmap);
                    return;
                }
                ImageDownloader downloader = new ImageDownloader(url, new OnImageLoadFinish() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        onImageLoadFinish.onSuccess(bitmap);
                    }

                    @Override
                    public void onError() {
                        onImageLoadFinish.onError();
                    }
                });
                downloader.execute();
            }
        }

        private static class ImageDownloader extends AsyncTask<Void, Void, Bitmap> {
            private String strUrl;
            private OnImageLoadFinish onImageLoadFinish;

            public ImageDownloader(String url, OnImageLoadFinish onImageLoadFinish) {
                this.strUrl = url;
                this.onImageLoadFinish = onImageLoadFinish;
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(strUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream in = urlConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) {
                    onImageLoadFinish.onSuccess(bitmap);
                } else {
                    onImageLoadFinish.onError();
                }
            }
        }

        public interface OnImageLoadFinish {
            public void onSuccess(Bitmap bitmap);

            public void onError();
        }
}

