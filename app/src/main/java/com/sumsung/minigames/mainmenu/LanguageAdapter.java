package com.sumsung.minigames.mainmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.sumsung.minigames.R;

import java.util.ArrayList;

public class LanguageAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<Drawable> languageImages;
    private Context context;

    public LanguageAdapter(Context context, ArrayList<Drawable> languageImages) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.languageImages = languageImages;
    }

    @Override
    public int getCount() {
        return languageImages.size();
    }

    @Override
    public Object getItem(int i) {
        return languageImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = layoutInflater
                .inflate(R.layout.language_item, viewGroup, false);

        ImageView spinnerImage = (ImageView) v.findViewById(R.id.language_image);
        spinnerImage.setImageDrawable(context.getDrawable(R.drawable.ic_languages));

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater
                .inflate(R.layout.language_item, parent, false);

        Drawable languageImage = (Drawable) getItem(position);
        ImageView spinnerImage = (ImageView) view.findViewById(R.id.language_image);
        spinnerImage.setImageDrawable(languageImage);

        return view;
    }
}
