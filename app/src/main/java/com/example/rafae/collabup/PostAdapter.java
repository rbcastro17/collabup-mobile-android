package com.example.rafae.collabup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Stefanie on 10/23/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
private TextView textname, textbody, textdate;
private ImageView imageAvatar;
    private Context mCtx;

private List<Post> postList;

public PostAdapter(Context mCtx, List<Post> postList){
 this.mCtx = mCtx;
 this.postList= postList;
}

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    LayoutInflater inflater = LayoutInflater.from(mCtx);
    View view = inflater.inflate(R.layout.cardlayout_post,null);
    return new PostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {

        final Post post = postList.get(position);

               holder.textname.setText(post.getName());
               holder.textbody.setText(post.getBody());
               holder.textdate.setText(post.getDate());

        Picasso.with(mCtx).load("https://collabup.org/images/"+post.getImage()).into(holder.imageAvatar);

       holder.textbody.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(mCtx, GroupIndexActivity.class);
               i.putExtra("body", post.getBody());
               mCtx.startActivity(i);
           }
       });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        TextView textname, textbody, textdate;
        ImageView imageAvatar;

            public PostViewHolder(View itemView)
            {
                    super(itemView);

               textname = (TextView) itemView.findViewById(R.id.textname);
               textbody = (TextView) itemView.findViewById(R.id.textbody);
               textdate = (TextView) itemView.findViewById(R.id.textdate);
               imageAvatar = (ImageView) itemView.findViewById(R.id.imageAvatar);

            }
     }

     public interface OnItemClickListener{
        void onItemClick(View view, int post_id);
    }
}

