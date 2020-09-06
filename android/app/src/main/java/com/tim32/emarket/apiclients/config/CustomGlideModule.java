package com.tim32.emarket.apiclients.config;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class CustomGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setMemoryCache(new LruResourceCache(1024 * 1024 * 10)); // 10 MB memory cache
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 1024 * 1024 * 100)); // 100 MB disk cache
        builder.setLogLevel(Log.ERROR);
    }
}
