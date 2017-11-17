package com.example.rafae.collabup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Stefanie on 11/8/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private TextView name, group_id, description;
    private Context mCtx;

    private List<Group> groupList;

    public GroupAdapter(Context mCtx, List<Group> groupList) {
        this.mCtx = mCtx;
        this.groupList = groupList;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardlayout_groups, null);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        final Group group = groupList.get(position);

        holder.group_id.setText(String.valueOf(group.getId()));
        holder.group_name.setText(group.getName());
        holder.description.setText(group.getDescription());
        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx, GroupListActivity.class);
                i.putExtra("description", group.getDescription());
                mCtx.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView group_id, group_name, description;

        public GroupViewHolder(View itemView) {
            super(itemView);

            group_id = (TextView) itemView.findViewById(R.id.group_id);
            group_name = (TextView) itemView.findViewById(R.id.group_name);
            description = (TextView) itemView.findViewById(R.id.description);

        }
    }
}
