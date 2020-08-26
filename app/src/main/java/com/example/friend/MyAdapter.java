package com.example.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.friend.databinding.ItemHeaderBinding;
import com.example.friend.databinding.ItemListBinding;

import java.util.List;

class Profile implements Comparable {
    int mIcon;
    String mName;
    Profile(int icon, String name) {
        this.mIcon = icon;
        this.mName = name;
    }

    @Override
    public int compareTo(Object o) {
        Profile profile = (Profile) o;
        return this.mName.compareTo(profile.mName);
    }

    public void setIcon(int icon) { this.mIcon = icon; }
    public int getIcon() { return mIcon; }

    public void setName(String name) { this.mName = name; }
    public String getName() { return mName; }
}

class MyHeaderHolder extends RecyclerView.ViewHolder {

    ItemHeaderBinding mBinding;

    MyHeaderHolder(ItemHeaderBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }
}

class MyListHolder extends RecyclerView.ViewHolder {

    ItemListBinding mBinding;

    MyListHolder(ItemListBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    // mBinding.getRoot().setOnClickListener(); 항목선택시
}

public class MyAdapter extends RecyclerView.Adapter {

    private List<Profile> mProfiles;
    private Context context;

    private static final int MY_HEADER = 0;
    private static final int MY_PROFILE = 1;
    private static final int FRIEND_HEADER = 2;

    MyAdapter(List<Profile> profiles, Context context) {
        mProfiles = profiles;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;

        if( viewType == MY_HEADER || viewType == FRIEND_HEADER ) { // header
            ItemHeaderBinding headerBinding = ItemHeaderBinding.inflate(LayoutInflater.from(context), parent, false);
            holder = new MyHeaderHolder(headerBinding);
        } else { // header가 아닐 때
            ItemListBinding listBinding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false);
            holder = new MyListHolder(listBinding);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == MY_HEADER && holder instanceof MyHeaderHolder) { // instanceof는 형변환 가능 여부.
            MyHeaderHolder mHolder = (MyHeaderHolder) holder;
            mHolder.mBinding.header.setText(mProfiles.get(position).getName());
        }
        else if (position == FRIEND_HEADER && holder instanceof MyHeaderHolder) {
            MyHeaderHolder mHolder = (MyHeaderHolder) holder;
            mHolder.mBinding.header.setText(mProfiles.get(position).getName());
        }
        else if (position == MY_PROFILE && holder instanceof MyListHolder) {
            MyListHolder mHolder = (MyListHolder) holder;
            mHolder.mBinding.image.setBackgroundResource(mProfiles.get(position).getIcon());
            // mHolder.mBinding.image.setImageResource(mProfiles.get(position).getIcon());
            mHolder.mBinding.name.setText(mProfiles.get(position).getName());
        }
        else {
            MyListHolder mHolder = (MyListHolder) holder;
            mHolder.mBinding.image.setBackgroundResource(mProfiles.get(position).getIcon());
            // mHolder.mBinding.image.setImageResource(mProfiles.get(position).getIcon());
            mHolder.mBinding.name.setText(mProfiles.get(position).getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MY_HEADER;
        } else if (position == 2) {
            return FRIEND_HEADER;
        } else {
            return position;
        }
    }

    @Override
    public int getItemCount() {
        return mProfiles.size(); // + 1
    }
}