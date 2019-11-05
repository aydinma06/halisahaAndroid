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
import halisahaandroid.mammy.halisahaandroid.model.calendarInfo;

public class calendarAdapter extends RecyclerView.Adapter<calendarAdapter.NumberViewHolder> {

    private static final String TAG = calendarAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;

    private int inNumberofItems;
    private static int viewHolderCount;
    calendarInfo[] calendarInfoArray;
    private static int colorValue;
    NumberViewHolder viewHolder;
    Context context;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public calendarAdapter(int numberOfItems,ListItemClickListener listener)
    {
        inNumberofItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rvfielditem;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        viewHolder = new NumberViewHolder(view);
        viewHolderCount = colorValue;
        //int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, viewHolderCount);
        //viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "+ viewHolderCount);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position)
    {
        final calendarInfo info = calendarInfoArray[position];
        Log.d(TAG, "#" + position);
        holder.bind(info);
    }

    @Override
    public int getItemCount()
    {
        if (calendarInfoArray != null) return calendarInfoArray.length;
        return 0;
    }
    public void setCalendarInfo(ArrayList<calendarInfo> veriler){
        this.calendarInfoArray = veriler.toArray(new calendarInfo[0]);
        notifyDataSetChanged();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemNumberView;
        TextView viewHolderIndex;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemNumberView =  itemView.findViewById(R.id.textViewFieldName);
            viewHolderIndex =  itemView.findViewById(R.id.textViewfieldAddress);
            itemView.setOnClickListener(this);
        }
        void bind(calendarInfo calendar) {
            int isEmpty = calendar.getFieldID();
            int actionType = calendar.getActionType();
            listItemNumberView.setText(calendar.getfieldName()+"-"+calendar.getfieldAddress());
            if(isEmpty == 0) {
                int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, 15);
                viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);
                viewHolderIndex.setText("Randevuya Uygun");
                colorValue = 15;
            }
            else if(actionType == 1){
                int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, 16);
                viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);
                viewHolderIndex.setText("İşletme onaylı");
                colorValue = 16;
            }
            else if (actionType == 2){
                int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, 17);
                viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);
                viewHolderIndex.setText("İşletme onayı bekleniyor");
                colorValue = 17;
            }
        }

        @Override
        public void onClick(View v) {
            int clickedpos = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedpos);
        }
    }
}
