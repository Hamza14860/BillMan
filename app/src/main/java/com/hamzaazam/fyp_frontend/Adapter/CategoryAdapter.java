package com.hamzaazam.fyp_frontend.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hamzaazam.fyp_frontend.Model.CategoryM;
import com.hamzaazam.fyp_frontend.R;
import com.hamzaazam.fyp_frontend.SingleCategoryActivity;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<CategoryM> mCategories;

    String catImageurl;



    public CategoryAdapter(Context context, List<CategoryM>  categoriesList){
        this.mContext=context;
        this.mCategories=categoriesList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.category_item,parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {

        final CategoryM category=mCategories.get(position);
        holder.catName.setText(category.getCatName());
        catImageurl=category.getCatUrl();

        if(catImageurl ==null || catImageurl.equals("None Chosen")){
            holder.categoryImage.setImageResource(R.mipmap.bill1);
        }
        else{
            Glide.with(mContext).load(catImageurl).into(holder.categoryImage);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent=new Intent(mContext, SingleCategoryActivity.class);
                    intent.putExtra("categoryid",mCategories.get(position).getCatId());
                    mContext.startActivity(intent);

            }
        });

        Animation animation= AnimationUtils.loadAnimation(mContext,android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);


    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        public ImageView categoryImage;
        public TextView catName;





        public CategoryViewHolder( View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.catImageItem);
            catName = itemView.findViewById(R.id.catNameItem);


        }
    }
}
