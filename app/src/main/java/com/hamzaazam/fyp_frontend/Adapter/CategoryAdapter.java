package com.hamzaazam.fyp_frontend.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hamzaazam.fyp_frontend.Fragments.AllBillsFragment;
import com.hamzaazam.fyp_frontend.Fragments.CreateCategoryFragment;
import com.hamzaazam.fyp_frontend.Model.CategoryM;
import com.hamzaazam.fyp_frontend.R;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<CategoryM> mCategories;

    String catImageurl;

    private Fragment frag;

    public CategoryAdapter(Context context, List<CategoryM>  categoriesList, Fragment cont){
        this.mContext=context;
        this.mCategories=categoriesList;

        frag=cont;
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

//                FragmentTransaction ft=getChildFragmentManager().beginTransaction();
//                CreateCategoryFragment ccf=new CreateCategoryFragment();
//                ft.replace(R.id.child_fragment_container,ccf);
//                //ft.addToBackStack(null);
//                ft.commit();


                ((AppCompatActivity) mContext).getSupportActionBar().setTitle(category.getCatName()+" OCR Bills");

                AllBillsFragment fragmentB = new AllBillsFragment();
                Bundle args = new Bundle();
                args.putString(AllBillsFragment.DATA_RECEIVE, mCategories.get(position).getCatName());
                fragmentB .setArguments(args);

                ((Fragment) frag).getChildFragmentManager().beginTransaction()
                        .replace(R.id.child_fragment_container, fragmentB)
                        .commit();

//                Intent intent=new Intent(mContext, SingleCategoryActivity.class);
//                    intent.putExtra("categoryid",mCategories.get(position).getCatId());
//                    mContext.startActivity(intent);

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
