package com.example.gswaf;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


//classe qui sert a afficher les éléments sur des list item
public class CustomListAdapter extends BaseAdapter {

    private List<Cocktail> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext,  List<Cocktail> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.like_item, null);
            holder = new ViewHolder();
            holder.CocktailPic = (ImageView) convertView.findViewById(R.id.imageView_CoktailPic);
            holder.CoktailName = (TextView) convertView.findViewById(R.id.textView_CoktailName);
            holder.CoktailIngredient = (TextView) convertView.findViewById(R.id.textView_ingredient);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cocktail cocktail = this.listData.get(position);
        holder.CoktailName.setText(cocktail.getName());
        holder.CoktailIngredient.setText("Ingredient: " + cocktail.getIngredients());

        int imageId = this.getMipmapResIdByName(cocktail.getImageURL());

        holder.CocktailPic.setImageResource(imageId);

        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    static class ViewHolder {
        ImageView CocktailPic;
        TextView CoktailName;
        TextView CoktailIngredient;
    }

}

