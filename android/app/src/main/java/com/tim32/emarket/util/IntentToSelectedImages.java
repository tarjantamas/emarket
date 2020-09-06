package com.tim32.emarket.util;

import android.content.Context;
import android.content.Intent;
import com.google.common.collect.Sets;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class IntentToSelectedImages {

    public static final String TAG = IntentToSelectedImages.class.getName();

    private final static Set<String> SUPPORTED_EXTENSIONS = Sets.newHashSet("jpg", "png", "jpeg", "bmp", "gif");

    private final Context context;

    private IntentToSelectedImages(Context context) {
        this.context = context;
    }

    public static IntentToSelectedImages with(Context context) {
        return new IntentToSelectedImages(context);
    }

    private Collection<String> getPaths(Intent intent) {
        List<String> locations = new ArrayList<>(extractMultiSelection(intent));
        addSingleSelection(intent, locations);
        return locations;
    }

    public List<File> getImages(Intent intent) {
        List<File> files = new ArrayList<>();
        for (String path : getPaths(intent)) {
            files.add(new File(path));
        }
        return files;
    }

    private void addSingleSelection(Intent intent, List<String> locations) {
        if (intent.getData() != null) {
            locations.add(intent.getData().getPath());
        }
    }

    private List<String> extractMultiSelection(Intent intent) {
        List<String> locations = new ArrayList<>();
        String path;
        if (intent.getClipData() != null) {
            for (int i = 0; i < intent.getClipData().getItemCount(); i++) {
                path = intent.getClipData().getItemAt(i).getUri().getPath();
                locations.add(path);
            }
        }
        return locations;
    }

    private boolean isValidPath(String path) {
        String extension = path.substring(path.lastIndexOf("."));
        return SUPPORTED_EXTENSIONS.contains(extension);
    }
}
