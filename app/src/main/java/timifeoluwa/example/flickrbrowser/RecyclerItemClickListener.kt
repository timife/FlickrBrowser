package timifeoluwa.example.flickrbrowser

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

//the  way to detect which item was clicked and what to do when clicked is now more difficult in recyclerView,
//but the most common way is to implement a RecyclerView.OnItemTouchListener interface in a class(here, simple)

private const val TAG = "RecyclerItemClickListen"

class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val listener: OnRecyclerClickListener
) : RecyclerView.SimpleOnItemTouchListener() {
    interface OnRecyclerClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    } //all clickListeners need something to call back on, so here we're calling back on interface onRecyclerClickListener.

    //add the gestureDetector
    //I'm creating an anonymous class that extends a simpleOnGestureListener then overriding the method i'm interested in.
    private val gestureDetector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.d(TAG, ".onSingleTapUp: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                Log.d(TAG, ".onSingleTapUp calling listener.onItemClick")
                listener.onItemClick(childView!!, recyclerView.getChildAdapterPosition(childView))
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                Log.d(TAG, ".onLongPress: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                Log.d(TAG, ".onItemLongClick calling listener.onItemClick")
                listener.onItemLongClick(
                    childView!!,
                    recyclerView.getChildAdapterPosition(childView)
                )
            }
        })

    //respond to a normal tap on the item, and onItemLongClick to respond to long clicks on the item.
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, ".onInterceptTouchEvent: starts $e")
        val result = gestureDetector.onTouchEvent(e)
        Log.d(TAG, ".onInterceptTouchEvent( returning: $result")
//        return super.onInterceptTouchEvent(rv, e)
        return result  //return 'true' means I'm telling the listener that I've handled every touch events, so nothing else needs to handle the events.If we don't return 'false', then the return super.onIntercept needs to be there so that
        //it gives anything else listening for events  a chance to respond.so, how do i decide which one to deal with, that's where the gesture detector comes in.
    }//this will return true
}