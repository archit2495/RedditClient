package com.example.architg.redditclientarchit;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by archit.g on 13/09/17.
 */

public class ScrollAwareBehaviorFAM extends CoordinatorLayout.Behavior<FloatingActionMenu> {
    private int toolbarHeight;
    public ScrollAwareBehaviorFAM(Context context, AttributeSet attrs){
        super();
    }
    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionMenu child, final View directTargetChild, final View target, final int nestedScrollAxes){
        return true;
    }
    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout,
                               final FloatingActionMenu child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,dxUnconsumed, dyUnconsumed);
        if(child.isOpened()){
            child.close(true);
        }
        if(dyConsumed > 0 && child.getVisibility() == View.VISIBLE){
            child.hideMenu(true);
        }else if(dyConsumed < 0 && child.getVisibility() != View.VISIBLE){
            child.showMenu(true);
        }
    }
}

