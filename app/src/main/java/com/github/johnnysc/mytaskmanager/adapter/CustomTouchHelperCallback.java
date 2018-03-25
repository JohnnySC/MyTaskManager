package com.github.johnnysc.mytaskmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Needs to handle drag and drop, swipe to dismiss items in recyclerView
 *
 * @author Asatryan on 25.03.18.
 */

public class CustomTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    private final TouchHelperInteractionListener mListener;

    public CustomTouchHelperCallback(int dragDirs, int swipeDirs, TouchHelperInteractionListener listener) {
        super(dragDirs, swipeDirs);
        mListener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mListener.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction != ItemTouchHelper.LEFT) {
            return;
        }
        mListener.onSwiped(viewHolder.getLayoutPosition());
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    public interface TouchHelperInteractionListener {

        void onMove(int oldPosition, int newPosition);

        void onSwiped(int position);
    }
}
