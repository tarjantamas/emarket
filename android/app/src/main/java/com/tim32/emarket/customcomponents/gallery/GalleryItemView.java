package com.tim32.emarket.customcomponents.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.config.GlideApp;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EView;

@SuppressLint("AppCompatCustomView")
@EView
public class GalleryItemView extends ImageView {

    @AfterViews
    void afterViews() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setScaleType(ScaleType.FIT_XY);
    }

    public GalleryItemView(Context context) {
        super(context);
    }

    public void bind(String imageUrl) {
        GlideApp.with(this).load(imageUrl).error(R.drawable.placeholder).into(this);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //pass width for both params in order to get square images
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
