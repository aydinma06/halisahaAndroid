package halisahaandroid.mammy.halisahaandroid.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import halisahaandroid.mammy.halisahaandroid.utils.ColorUtils;
import halisahaandroid.mammy.halisahaandroid.R;
import halisahaandroid.mammy.halisahaandroid.model.fieldInfo;

public class rViewAdapter extends RecyclerView.Adapter<rViewAdapter.NumberViewHolder> {

    private static final String TAG = rViewAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;

    private int inNumberofItems;
    private static int viewHolderCount;
    fieldInfo[] userInfoArray;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public rViewAdapter(int numberOfItems,ListItemClickListener listener)
    {
        inNumberofItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rvfielditem12;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, viewHolderCount);
        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);
        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "+ viewHolderCount);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position)
    {
        final fieldInfo info = userInfoArray[position];
        Log.d(TAG, "#" + position);
        holder.bind(info);
    }

    @Override
    public int getItemCount()
    {
        if (userInfoArray != null) return userInfoArray.length;
        return 0;
    }
    public void setUserInfo(ArrayList<fieldInfo> veriler){
        this.userInfoArray = veriler.toArray(new fieldInfo[0]);
        notifyDataSetChanged();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemNumberView;
        TextView viewHolderIndex;
        TextView phoneNumberView;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemNumberView =  itemView.findViewById(R.id.textViewFieldName);
            viewHolderIndex =  itemView.findViewById(R.id.textViewfieldAddress);
            phoneNumberView = itemView.findViewById(R.id.textViewFieldName2);
            itemView.setOnClickListener(this);
        }
        void bind(fieldInfo user) {

            listItemNumberView.setText(user.getfieldName());
            viewHolderIndex.setText(user.getfieldAddress());
            phoneNumberView.setText("Telefon Numarası : "+user.getFieldPhone());
        }

        @Override
        public void onClick(View v) {
            int clickedpos = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedpos);
        }
    }
}
