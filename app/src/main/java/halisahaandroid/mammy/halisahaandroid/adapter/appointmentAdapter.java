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
import halisahaandroid.mammy.halisahaandroid.model.appointmentInfo;

public class appointmentAdapter extends RecyclerView.Adapter<appointmentAdapter.NumberViewHolder> {

    private static final String TAG = appointmentAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;

    private int inNumberofItems;
    private static int viewHolderCount;
    appointmentInfo[] appointmentInfoArray;
    private static int colorValue;
    NumberViewHolder viewHolder;
    Context context;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public appointmentAdapter(int numberOfItems,ListItemClickListener listener)
    {
        inNumberofItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rvfielditem12;
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
        final appointmentInfo info = appointmentInfoArray[position];
        Log.d(TAG, "#" + position);
        holder.bind(info);
    }

    @Override
    public int getItemCount()
    {
        if (appointmentInfoArray != null) return appointmentInfoArray.length;
        return 0;
    }
    public void setAppointmentInfo(ArrayList<appointmentInfo> veriler){
        this.appointmentInfoArray = veriler.toArray(new appointmentInfo[0]);
        notifyDataSetChanged();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemNumberView;
        TextView viewHolderIndex;
        TextView date;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemNumberView =  itemView.findViewById(R.id.textViewFieldName);
            viewHolderIndex =  itemView.findViewById(R.id.textViewfieldAddress);
            date =itemView.findViewById(R.id.textViewFieldName2);
            itemView.setOnClickListener(this);
        }
        void bind(appointmentInfo appointment) {
            String id = String.valueOf(appointment.getFieldID());
            int actionType = appointment.getActionType();
            listItemNumberView.setText(id);

            String s1 = appointment.getStartDate()+"-"+appointment.getFinishDate();
            date.setText(s1);
            if(actionType==1){
                int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, 15);
                viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);
                viewHolderIndex.setText("Randevuya Uygun");
            viewHolderIndex.setText("Onaylandı");
            }
            else if(actionType==2){viewHolderIndex.setText("İstek beklemede");
                int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, 17);
                viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);}
            else if(actionType==3){viewHolderIndex.setText("İstek tarafınızca iptal edildi");
                int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, 16);
                viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);}
            else if(actionType==4){viewHolderIndex.setText("İstek işletme tarafından iptal edildi");
                int backgroundColorForViewHolder = ColorUtils.getViewHolderBackgroundColorFromInstance(context, 16);
                viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);}
            }
        @Override
        public void onClick(View v) {
            int clickedpos = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedpos);
        }
    }
}

