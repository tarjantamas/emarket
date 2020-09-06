package com.tim32.emarket.customcomponents.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.config.GlideApp;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ImageSliderAdapter extends
        SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    private final Context context;

    private List<String> imageUrls;

    public ImageSliderAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    public void setItems(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.imageUrls.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String imageUrl) {
        this.imageUrls.add(imageUrl);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        String imageUrl = imageUrls.get(position);

        GlideApp.with(viewHolder.itemView)
                .load(imageUrl)
                .fitCenter()
                .error(R.drawable.placeholder)
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        if (CollectionUtils.isEmpty(imageUrls)) {
            return 0;
        }
        return imageUrls.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
