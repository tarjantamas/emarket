package com.tim32.emarket.customcomponents.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private List<String> imageUrls;

    private final Context context;

    public GalleryAdapter(Context context, List<String> urls) {
        this.context = context;
        imageUrls = urls;
    }


    @Override
    public int getCount() {
        if (CollectionUtils.isEmpty(imageUrls)) {
            return 0;
        }
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GalleryItemView galleryItemView;
        if (convertView == null) {
            galleryItemView = GalleryItemView_.build(context);
        } else {
            galleryItemView = (GalleryItemView) convertView;
        }

        String imageUrl = (String) getItem(position);
        galleryItemView.bind(imageUrl);
        return galleryItemView;
    }

    public void addItem(String imagePath) {
        imageUrls.add(imagePath);
        notifyDataSetChanged();
    }

    public void deleteItem(String id) {
        imageUrls.remove(id);
        notifyDataSetChanged();
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }
}
