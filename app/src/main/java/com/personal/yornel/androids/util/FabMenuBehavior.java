package com.personal.yornel.androids.util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by Yornel on 22/9/2017.
 */

public class FabMenuBehavior extends CoordinatorLayout.Behavior<FloatingActionMenu> {

    private int accumulator = 0;
    private int threshold = 0;

    public FabMenuBehavior() {
        super();
    }

    public FabMenuBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionMenu child, View directTargetChild, View target, int nestedScrollAxes) {
        threshold = (child.getChildCount() > 0 ? child.getChildAt(0).getHeight() : child.getHeight()) / 2;
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if ((accumulator * dyConsumed) < 0) { //scroll direction change
            accumulator = 0;
        }
        accumulator += dyConsumed;

        if (accumulator > threshold && !child.isMenuButtonHidden()) {
            child.hideMenuButton(true);
        } else if (accumulator < -threshold && child.isMenuButtonHidden()) {
            child.showMenuButton(true);
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionMenu child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        accumulator = 0;
    }

}
