package com.testography.amgradle.flow;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.testography.amgradle.R;
import com.testography.amgradle.mortar.ScreenScoper;

import java.util.Collections;
import java.util.Map;

import flow.Direction;
import flow.Dispatcher;
import flow.KeyChanger;
import flow.State;
import flow.Traversal;
import flow.TraversalCallback;
import flow.TreeKey;

public class TreeKeyDispatcher implements KeyChanger, Dispatcher {

    private Activity mActivity;
    private Object inKey;
    @Nullable
    private Object outKey;
    private FrameLayout mRootFrame;

    public TreeKeyDispatcher(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void dispatch(@NonNull Traversal traversal,
                         @NonNull TraversalCallback callback) {
        Map<Object, Context> contexts;
        State inState = traversal.getState(traversal.destination.top());
        inKey = inState.getKey();
        State outState = traversal.origin == null ? null : traversal.getState
                (traversal.origin.top());
        outKey = outState == null ? null : outState.getKey();

        mRootFrame = (FrameLayout) mActivity.findViewById(R.id.root_frame);

        if (inKey.equals(outKey)) {
            callback.onTraversalCompleted();
            return;
        }

        if (inKey instanceof TreeKey) {
            // TODO: 25-Nov-16 implement treekey case for those who will rewriter ScreenScoper
        }

        Context flowContext = traversal.createContext(inKey, mActivity);
        Context mortarContext = ScreenScoper.getScreenScope((AbstractScreen)
                inKey).createContext(flowContext);
        contexts = Collections.singletonMap(inKey, mortarContext);
        changeKey(outState, inState, traversal.direction, contexts, callback);
    }

    @Override
    public void changeKey(@Nullable State outgoingState,
                          @NonNull State incomingState,
                          @NonNull Direction direction,
                          @NonNull Map<Object, Context> incomingContexts,
                          @NonNull TraversalCallback callback) {

        Context context = incomingContexts.get(inKey);

        //save prev View
        if (outgoingState != null) {
            outgoingState.save(mRootFrame.getChildAt(0));
        }

        //create new view
        Screen screen;
        screen = inKey.getClass().getAnnotation(Screen.class);
        if (screen == null) {
            throw new IllegalStateException("@Screen annotation is missing on " +
                    "screen " + ((AbstractScreen) inKey).getScopeName());
        } else {
            int layout = screen.value();

            LayoutInflater inflater = LayoutInflater.from(context);
            View newView = inflater.inflate(layout, mRootFrame, false);

            //restore state to new view
            incomingState.restore(newView);

            //delete old view
            if ((outKey) != null && !(inKey instanceof TreeKey)) {
                ((AbstractScreen) outKey).unregisterScope();
            }

            if (mRootFrame.getChildAt(0) != null) {
                mRootFrame.removeView(mRootFrame.getChildAt(0));
            }

            mRootFrame.addView(newView);
            callback.onTraversalCompleted();
        }
    }
}
