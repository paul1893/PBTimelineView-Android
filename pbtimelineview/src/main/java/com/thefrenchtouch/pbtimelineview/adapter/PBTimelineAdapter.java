package com.thefrenchtouch.pbtimelineview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.thefrenchtouch.pbtimelineview.R;
import com.thefrenchtouch.pbtimelineview.object.PBItem;
import com.thefrenchtouch.pbtimelineview.widget.PBTimelineView;

import java.util.ArrayList;

/**
 * Created by Paul on 09/07/16.
 */
public class PBTimelineAdapter extends RecyclerView.Adapter<PBTimelineAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PBItem> items; //The data of the adapter
    private PBTimelineView.onItemClickListener listener;
    private static final int TYPE_ITEM = 1; //Type of the item

    /**
     * Constructor
     *
     * @param context
     * @param items
     */
    // Provide a suitable constructor (depends on the kind of dataset)
    public PBTimelineAdapter(Context context, ArrayList<PBItem> items, PBTimelineView.onItemClickListener listener) {
        this.mContext = context;
        this.items = items;
        this.listener = listener;
    }

    /**
     * Create the row
     *
     * @param parent
     * @param viewType
     */
    // Create new views (invoked by the layout manager)
    @Override
    public PBTimelineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        ViewHolder vh = null;

        //We check if it's an item type
        if (viewType == TYPE_ITEM) {
            //From XML
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pbtimelineadapter_pbitemview, parent, false);
            view.getLayoutParams().height = items.get(0).getItemHeight();
            view.getLayoutParams().width = items.get(0).getItemWidth();

            // Or Programmatically
//            PBItem item = getItem(0);
//            PBItemView view = new PBItemView(mContext, item);
//            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(item.getItemWidth(), item.getItemHeight());
//            int margin = mContext.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
//            params.setMargins(margin, 0, margin, 0);
//            view.setLayoutParams(params);

            vh = new VHItem(view);
        }

        return vh;
    }

    /**
     * We put the data in the items
     */
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof VHItem) {
            // Cast holder to VHItem and set data
            VHItem mHolder = ((VHItem) holder);

            // Get the current text
            PBItem item = getItem(position);

            // Set the view
            mHolder.item = item;
            mHolder.view.setBackgroundColor(item.getBackgroundColor());

            if (mHolder.text != null) {
                mHolder.text.setText(item.getText());
            } else {
                mHolder.view.invalidate();
            }
        }

    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Return the type of the item
     */
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    /**
     * Get the "real" item because everything is shift with the header
     *
     * @param position position you choose
     * @return Message The message of the item
     */
    private PBItem getItem(int position) {
        return items.get(position);
    }


//    /**
//     * Set new data to the adapter (and automatically notify data has been changed)
//     *
//     * @param newDataSet
//     * */
//    public void setDataSet(String[] newDataSet){
//        //this.strings = newDataSet;
//        //notifyDataSetChanged();
//    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    /**
     * Class of an item
     *
     * @author Paul
     */
    class VHItem extends PBTimelineAdapter.ViewHolder implements View.OnClickListener {
        public PBItem item;
        public View view;
        public TextView text;

        public VHItem(View itemView) {
            super(itemView);
            this.view = itemView;
            this.text = (TextView) itemView.findViewById(R.id.pbitemview_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(item.getSection(), item.getId(), item.getText(), item);
        }
    }

}
