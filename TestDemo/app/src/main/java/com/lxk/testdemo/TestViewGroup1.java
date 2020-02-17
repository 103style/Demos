package com.lxk.testdemo;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewParent;
import android.view.ViewStructure;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author xiaoke.luo@tcl.com 2020/1/17 16:58
 */
public class TestViewGroup extends ViewGroup {
    private static final String TAG = "TestViewGroup";

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, " onLayout(boolean changed, int l, int t, int r, int b)");
    }

    @Override
    public void requestLayout() {
        Log.e(TAG, " requestLayout()");
        super.requestLayout();
    }

    @Override
    public void updateViewLayout(View view, LayoutParams params) {
        Log.e(TAG, " updateViewLayout(View view, LayoutParams params)");
        super.updateViewLayout(view, params);
    }

    @Override
    public int getDescendantFocusability() {
        Log.e(TAG, " getDescendantFocusability()");
        return super.getDescendantFocusability();
    }

    @Override
    public void setDescendantFocusability(int focusability) {
        Log.e(TAG, " setDescendantFocusability(int focusability)");
        super.setDescendantFocusability(focusability);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        Log.e(TAG, " requestChildFocus(View child, View focused)");
        super.requestChildFocus(child, focused);
    }

    @Override
    public void focusableViewAvailable(View v) {
        Log.e(TAG, " focusableViewAvailable(View v)");
        super.focusableViewAvailable(v);
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        Log.e(TAG, " showContextMenuForChild(View originalView)");
        return super.showContextMenuForChild(originalView);
    }

    @Override
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        Log.e(TAG, " showContextMenuForChild(View originalView, float x, float y)");
        return super.showContextMenuForChild(originalView, x, y);
    }

    @Override
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        Log.e(TAG, " startActionModeForChild(View originalView, ActionMode.Callback callback)");
        return super.startActionModeForChild(originalView, callback);
    }

    @Override
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback, int type) {
        Log.e(TAG, " startActionModeForChild(View originalView, ActionMode.Callback callback, int type)");
        return super.startActionModeForChild(originalView, callback, type);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        Log.e(TAG, " focusSearch(View focused, int direction)");
        return super.focusSearch(focused, direction);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        Log.e(TAG, " requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate)");
        return super.requestChildRectangleOnScreen(child, rectangle, immediate);
    }

    @Override
    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        Log.e(TAG, " requestSendAccessibilityEvent(View child, AccessibilityEvent event)");
        return super.requestSendAccessibilityEvent(child, event);
    }

    @Override
    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        Log.e(TAG, " onRequestSendAccessibilityEvent(View child, AccessibilityEvent event)");
        return super.onRequestSendAccessibilityEvent(child, event);
    }

    @Override
    public void childHasTransientStateChanged(View child, boolean childHasTransientState) {
        Log.e(TAG, " childHasTransientStateChanged(View child, boolean childHasTransientState)");
        super.childHasTransientStateChanged(child, childHasTransientState);
    }

    @Override
    public boolean hasTransientState() {
        Log.e(TAG, " hasTransientState()");
        return super.hasTransientState();
    }

    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        Log.e(TAG, " dispatchUnhandledMove(View focused, int direction)");
        return super.dispatchUnhandledMove(focused, direction);
    }

    @Override
    public void clearChildFocus(View child) {
        Log.e(TAG, " clearChildFocus(View child)");
        super.clearChildFocus(child);
    }

    @Override
    public void clearFocus() {
        Log.e(TAG, " clearFocus()");
        super.clearFocus();
    }

    @Override
    public View getFocusedChild() {
        Log.e(TAG, " getFocusedChild()");
        return super.getFocusedChild();
    }

    @Override
    public boolean hasFocus() {
        Log.e(TAG, " hasFocus()");
        return super.hasFocus();
    }

    @Override
    public View findFocus() {
        Log.e(TAG, " findFocus()");
        return super.findFocus();
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        Log.e(TAG, " addFocusables(ArrayList<View> views, int direction, int focusableMode)");
        super.addFocusables(views, direction, focusableMode);
    }

    @Override
    public void addKeyboardNavigationClusters(Collection<View> views, int direction) {
        Log.e(TAG, " addKeyboardNavigationClusters(Collection<View> views, int direction)");
        super.addKeyboardNavigationClusters(views, direction);
    }

    @Override
    public boolean getTouchscreenBlocksFocus() {
        Log.e(TAG, " getTouchscreenBlocksFocus()");
        return super.getTouchscreenBlocksFocus();
    }

    @Override
    public void setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus) {
        Log.e(TAG, " setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus)");
        super.setTouchscreenBlocksFocus(touchscreenBlocksFocus);
    }

    @Override
    public void findViewsWithText(ArrayList<View> outViews, CharSequence text, int flags) {
        Log.e(TAG, " findViewsWithText(ArrayList<View> outViews, CharSequence text, int flags)");
        super.findViewsWithText(outViews, text, flags);
    }

    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        Log.e(TAG, " dispatchWindowFocusChanged(boolean hasFocus)");
        super.dispatchWindowFocusChanged(hasFocus);
    }

    @Override
    public void addTouchables(ArrayList<View> views) {
        Log.e(TAG, " addTouchables(ArrayList<View> views)");
        super.addTouchables(views);
    }

    @Override
    public void dispatchDisplayHint(int hint) {
        Log.e(TAG, " dispatchDisplayHint(int hint)");
        super.dispatchDisplayHint(hint);
    }

    @Override
    protected void dispatchVisibilityChanged(View changedView, int visibility) {
        Log.e(TAG, " dispatchVisibilityChanged(View changedView, int visibility)");
        super.dispatchVisibilityChanged(changedView, visibility);
    }

    @Override
    public void dispatchWindowVisibilityChanged(int visibility) {
        Log.e(TAG, " dispatchWindowVisibilityChanged(int visibility)");
        super.dispatchWindowVisibilityChanged(visibility);
    }

    @Override
    public void dispatchConfigurationChanged(Configuration newConfig) {
        Log.e(TAG, " dispatchConfigurationChanged(Configuration newConfig)");
        super.dispatchConfigurationChanged(newConfig);
    }

    @Override
    public void recomputeViewAttributes(View child) {
        Log.e(TAG, " recomputeViewAttributes(View child)");
        super.recomputeViewAttributes(child);
    }

    @Override
    public void bringChildToFront(View child) {
        Log.e(TAG, " bringChildToFront(View child)");
        super.bringChildToFront(child);
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        Log.e(TAG, " dispatchDragEvent(DragEvent event)");
        return super.dispatchDragEvent(event);
    }

    @Override
    public void dispatchWindowSystemUiVisiblityChanged(int visible) {
        Log.e(TAG, " dispatchWindowSystemUiVisiblityChanged(int visible)");
        super.dispatchWindowSystemUiVisiblityChanged(visible);
    }

    @Override
    public void dispatchSystemUiVisibilityChanged(int visible) {
        Log.e(TAG, " dispatchSystemUiVisibilityChanged(int visible)");
        super.dispatchSystemUiVisibilityChanged(visible);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        Log.e(TAG, " dispatchKeyEventPreIme(KeyEvent event)");
        return super.dispatchKeyEventPreIme(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e(TAG, " dispatchKeyEvent(KeyEvent event)");
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        Log.e(TAG, " dispatchKeyShortcutEvent(KeyEvent event)");
        return super.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        Log.e(TAG, " dispatchTrackballEvent(MotionEvent event)");
        return super.dispatchTrackballEvent(event);
    }

    @Override
    public boolean dispatchCapturedPointerEvent(MotionEvent event) {
        Log.e(TAG, " dispatchCapturedPointerEvent(MotionEvent event)");
        return super.dispatchCapturedPointerEvent(event);
    }

    @Override
    public void dispatchPointerCaptureChanged(boolean hasCapture) {
        Log.e(TAG, " dispatchPointerCaptureChanged(boolean hasCapture)");
        super.dispatchPointerCaptureChanged(hasCapture);
    }

    @Override
    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        Log.e(TAG, " onResolvePointerIcon(MotionEvent event, int pointerIndex)");
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        Log.e(TAG, " dispatchHoverEvent(MotionEvent event)");
        return super.dispatchHoverEvent(event);
    }

    @Override
    public void addChildrenForAccessibility(ArrayList<View> outChildren) {
        Log.e(TAG, " addChildrenForAccessibility(ArrayList<View> outChildren)");
        super.addChildrenForAccessibility(outChildren);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        Log.e(TAG, " onInterceptHoverEvent(MotionEvent event)");
        return super.onInterceptHoverEvent(event);
    }

    @Override
    protected boolean dispatchGenericPointerEvent(MotionEvent event) {
        Log.e(TAG, " dispatchGenericPointerEvent(MotionEvent event)");
        return super.dispatchGenericPointerEvent(event);
    }

    @Override
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        Log.e(TAG, " dispatchGenericFocusedEvent(MotionEvent event)");
        return super.dispatchGenericFocusedEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, " dispatchTouchEvent(MotionEvent ev)");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean isMotionEventSplittingEnabled() {
        Log.e(TAG, " isMotionEventSplittingEnabled()");
        return super.isMotionEventSplittingEnabled();
    }

    @Override
    public void setMotionEventSplittingEnabled(boolean split) {
        Log.e(TAG, " setMotionEventSplittingEnabled(boolean split)");
        super.setMotionEventSplittingEnabled(split);
    }

    @Override
    public boolean isTransitionGroup() {
        Log.e(TAG, " isTransitionGroup()");
        return super.isTransitionGroup();
    }

    @Override
    public void setTransitionGroup(boolean isTransitionGroup) {
        Log.e(TAG, " setTransitionGroup(boolean isTransitionGroup)");
        super.setTransitionGroup(isTransitionGroup);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.e(TAG, " requestDisallowInterceptTouchEvent(boolean disallowIntercept)");
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, " onInterceptTouchEvent(MotionEvent ev)");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        Log.e(TAG, " requestFocus(int direction, Rect previouslyFocusedRect)");
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        Log.e(TAG, " onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect)");
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public boolean restoreDefaultFocus() {
        Log.e(TAG, " restoreDefaultFocus()");
        return super.restoreDefaultFocus();
    }

    @Override
    public void dispatchStartTemporaryDetach() {
        Log.e(TAG, " dispatchStartTemporaryDetach()");
        super.dispatchStartTemporaryDetach();
    }

    @Override
    public void dispatchFinishTemporaryDetach() {
        Log.e(TAG, " dispatchFinishTemporaryDetach()");
        super.dispatchFinishTemporaryDetach();
    }

    @Override
    public void dispatchProvideStructure(ViewStructure structure) {
        Log.e(TAG, " dispatchProvideStructure(ViewStructure structure)");
        super.dispatchProvideStructure(structure);
    }

    @Override
    public void dispatchProvideAutofillStructure(ViewStructure structure, int flags) {
        Log.e(TAG, " dispatchProvideAutofillStructure(ViewStructure structure, int flags)");
        super.dispatchProvideAutofillStructure(structure, flags);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        Log.e(TAG, " getAccessibilityClassName()");
        return super.getAccessibilityClassName();
    }

    @Override
    public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
        Log.e(TAG, " notifySubtreeAccessibilityStateChanged(View child, View source, int changeType)");
        super.notifySubtreeAccessibilityStateChanged(child, source, changeType);
    }

    @Override
    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        Log.e(TAG, " onNestedPrePerformAccessibilityAction(View target, int action, Bundle args)");
        return super.onNestedPrePerformAccessibilityAction(target, action, args);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        Log.e(TAG, " dispatchSaveInstanceState(SparseArray<Parcelable> container)");
        super.dispatchSaveInstanceState(container);
    }

    @Override
    protected void dispatchFreezeSelfOnly(SparseArray<Parcelable> container) {
        Log.e(TAG, " dispatchFreezeSelfOnly(SparseArray<Parcelable> container)");
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        Log.e(TAG, " dispatchRestoreInstanceState(SparseArray<Parcelable> container)");
        super.dispatchRestoreInstanceState(container);
    }

    @Override
    protected void dispatchThawSelfOnly(SparseArray<Parcelable> container) {
        Log.e(TAG, " dispatchThawSelfOnly(SparseArray<Parcelable> container)");
        super.dispatchThawSelfOnly(container);
    }

    @Override
    protected void setChildrenDrawingCacheEnabled(boolean enabled) {
        Log.e(TAG, " setChildrenDrawingCacheEnabled(boolean enabled)");
        super.setChildrenDrawingCacheEnabled(enabled);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.e(TAG, " dispatchDraw(Canvas canvas)");
        super.dispatchDraw(canvas);
    }

    @Override
    public ViewGroupOverlay getOverlay() {
        Log.e(TAG, " getOverlay()");
        return super.getOverlay();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int drawingPosition) {
        Log.e(TAG, " getChildDrawingOrder(int childCount, int drawingPosition)");
        return super.getChildDrawingOrder(childCount, drawingPosition);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Log.e(TAG, " drawChild(Canvas canvas, View child, long drawingTime)");
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    public boolean getClipChildren() {
        Log.e(TAG, " getClipChildren()");
        return super.getClipChildren();
    }

    @Override
    public void setClipChildren(boolean clipChildren) {
        Log.e(TAG, " setClipChildren(boolean clipChildren)");
        super.setClipChildren(clipChildren);
    }

    @Override
    public boolean getClipToPadding() {
        Log.e(TAG, " getClipToPadding()");
        return super.getClipToPadding();
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        Log.e(TAG, " setClipToPadding(boolean clipToPadding)");
        super.setClipToPadding(clipToPadding);
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        Log.e(TAG, " dispatchSetSelected(boolean selected)");
        super.dispatchSetSelected(selected);
    }

    @Override
    public void dispatchSetActivated(boolean activated) {
        Log.e(TAG, " dispatchSetActivated(boolean activated)");
        super.dispatchSetActivated(activated);
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        Log.e(TAG, " dispatchSetPressed(boolean pressed)");
        super.dispatchSetPressed(pressed);
    }

    @Override
    public void dispatchDrawableHotspotChanged(float x, float y) {
        Log.e(TAG, " dispatchDrawableHotspotChanged(float x, float y)");
        super.dispatchDrawableHotspotChanged(x, y);
    }

    @Override
    protected void setStaticTransformationsEnabled(boolean enabled) {
        Log.e(TAG, " setStaticTransformationsEnabled(boolean enabled)");
        super.setStaticTransformationsEnabled(enabled);
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        Log.e(TAG, " getChildStaticTransformation(View child, Transformation t)");
        return super.getChildStaticTransformation(child, t);
    }

    @Override
    public void addView(View child) {
        Log.e(TAG, " addView(View child)");
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        Log.e(TAG, " addView(View child, int index)");
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        Log.e(TAG, " addView(View child, int width, int height)");
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, LayoutParams params) {
        Log.e(TAG, " addView(View child, LayoutParams params)");
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        Log.e(TAG, " addView(View child, int index, LayoutParams params)");
        super.addView(child, index, params);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        Log.e(TAG, " checkLayoutParams(LayoutParams p)");
        return super.checkLayoutParams(p);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        Log.e(TAG, " setOnHierarchyChangeListener(OnHierarchyChangeListener listener)");
        super.setOnHierarchyChangeListener(listener);
    }

    @Override
    public void onViewAdded(View child) {
        Log.e(TAG, " onViewAdded(View child)");
        super.onViewAdded(child);
    }

    @Override
    public void onViewRemoved(View child) {
        Log.e(TAG, " onViewRemoved(View child)");
        super.onViewRemoved(child);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.e(TAG, " onAttachedToWindow()");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e(TAG, " onDetachedFromWindow()");
        super.onDetachedFromWindow();
    }

    @Override
    protected boolean addViewInLayout(View child, int index, LayoutParams params) {
        Log.e(TAG, " addViewInLayout(View child, int index, LayoutParams params)");
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        Log.e(TAG, " addViewInLayout(View child, int index, LayoutParams params, boolean preventRequestLayout)");
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    @Override
    protected void cleanupLayoutState(View child) {
        Log.e(TAG, " cleanupLayoutState(View child)");
        super.cleanupLayoutState(child);
    }

    @Override
    protected void attachLayoutAnimationParameters(View child, LayoutParams params, int index, int count) {
        Log.e(TAG, " attachLayoutAnimationParameters(View child, LayoutParams params, int index, int count)");
        super.attachLayoutAnimationParameters(child, params, index, count);
    }

    @Override
    public void removeView(View view) {
        Log.e(TAG, " removeView(View view)");
        super.removeView(view);
    }

    @Override
    public void removeViewInLayout(View view) {
        Log.e(TAG, " removeViewInLayout(View view)");
        super.removeViewInLayout(view);
    }

    @Override
    public void removeViewsInLayout(int start, int count) {
        Log.e(TAG, " removeViewsInLayout(int start, int count)");
        super.removeViewsInLayout(start, count);
    }

    @Override
    public void removeViewAt(int index) {
        Log.e(TAG, " removeViewAt(int index)");
        super.removeViewAt(index);
    }

    @Override
    public void removeViews(int start, int count) {
        Log.e(TAG, " removeViews(int start, int count)");
        super.removeViews(start, count);
    }

    @Override
    public LayoutTransition getLayoutTransition() {
        Log.e(TAG, " getLayoutTransition()");
        return super.getLayoutTransition();
    }

    @Override
    public void setLayoutTransition(LayoutTransition transition) {
        Log.e(TAG, " setLayoutTransition(LayoutTransition transition)");
        super.setLayoutTransition(transition);
    }

    @Override
    public void removeAllViews() {
        Log.e(TAG, " removeAllViews()");
        super.removeAllViews();
    }

    @Override
    public void removeAllViewsInLayout() {
        Log.e(TAG, " removeAllViewsInLayout()");
        super.removeAllViewsInLayout();
    }

    @Override
    protected void removeDetachedView(View child, boolean animate) {
        Log.e(TAG, " removeDetachedView(View child, boolean animate)");
        super.removeDetachedView(child, animate);
    }

    @Override
    protected void attachViewToParent(View child, int index, LayoutParams params) {
        Log.e(TAG, " attachViewToParent(View child, int index, LayoutParams params)");
        super.attachViewToParent(child, index, params);
    }

    @Override
    protected void detachViewFromParent(View child) {
        Log.e(TAG, " detachViewFromParent(View child)");
        super.detachViewFromParent(child);
    }

    @Override
    protected void detachViewFromParent(int index) {
        Log.e(TAG, " detachViewFromParent(int index)");
        super.detachViewFromParent(index);
    }

    @Override
    protected void detachViewsFromParent(int start, int count) {
        Log.e(TAG, " detachViewsFromParent(int start, int count)");
        super.detachViewsFromParent(start, count);
    }

    @Override
    protected void detachAllViewsFromParent() {
        Log.e(TAG, " detachAllViewsFromParent()");
        super.detachAllViewsFromParent();
    }

    @Override
    public void onDescendantInvalidated(@NonNull View child, @NonNull View target) {
        Log.e(TAG, " onDescendantInvalidated(@NonNull View child, @NonNull View target)");
        super.onDescendantInvalidated(child, target);
    }

    @Override
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        Log.e(TAG, " invalidateChildInParent(int[] location, Rect dirty)");
        return super.invalidateChildInParent(location, dirty);
    }

    @Override
    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        Log.e(TAG, " getChildVisibleRect(View child, Rect r, Point offset)");
        return super.getChildVisibleRect(child, r, offset);
    }

    @Override
    protected boolean canAnimate() {
        Log.e(TAG, " canAnimate()");
        return super.canAnimate();
    }

    @Override
    public void startLayoutAnimation() {
        Log.e(TAG, " startLayoutAnimation()");
        super.startLayoutAnimation();
    }

    @Override
    public void scheduleLayoutAnimation() {
        Log.e(TAG, " scheduleLayoutAnimation()");
        super.scheduleLayoutAnimation();
    }

    @Override
    public LayoutAnimationController getLayoutAnimation() {
        Log.e(TAG, " getLayoutAnimation()");
        return super.getLayoutAnimation();
    }

    @Override
    public void setLayoutAnimation(LayoutAnimationController controller) {
        Log.e(TAG, " setLayoutAnimation(LayoutAnimationController controller)");
        super.setLayoutAnimation(controller);
    }

    @Override
    public boolean isAnimationCacheEnabled() {
        Log.e(TAG, " isAnimationCacheEnabled()");
        return super.isAnimationCacheEnabled();
    }

    @Override
    public void setAnimationCacheEnabled(boolean enabled) {
        Log.e(TAG, " setAnimationCacheEnabled(boolean enabled)");
        super.setAnimationCacheEnabled(enabled);
    }

    @Override
    public boolean isAlwaysDrawnWithCacheEnabled() {
        Log.e(TAG, " isAlwaysDrawnWithCacheEnabled()");
        return super.isAlwaysDrawnWithCacheEnabled();
    }

    @Override
    public void setAlwaysDrawnWithCacheEnabled(boolean always) {
        Log.e(TAG, " setAlwaysDrawnWithCacheEnabled(boolean always)");
        super.setAlwaysDrawnWithCacheEnabled(always);
    }

    @Override
    protected boolean isChildrenDrawnWithCacheEnabled() {
        Log.e(TAG, " isChildrenDrawnWithCacheEnabled()");
        return super.isChildrenDrawnWithCacheEnabled();
    }

    @Override
    protected void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        Log.e(TAG, " setChildrenDrawnWithCacheEnabled(boolean enabled)");
        super.setChildrenDrawnWithCacheEnabled(enabled);
    }

    @Override
    protected boolean isChildrenDrawingOrderEnabled() {
        Log.e(TAG, " isChildrenDrawingOrderEnabled()");
        return super.isChildrenDrawingOrderEnabled();
    }

    @Override
    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        Log.e(TAG, " setChildrenDrawingOrderEnabled(boolean enabled)");
        super.setChildrenDrawingOrderEnabled(enabled);
    }

    @Override
    public int getPersistentDrawingCache() {
        Log.e(TAG, " getPersistentDrawingCache()");
        return super.getPersistentDrawingCache();
    }

    @Override
    public void setPersistentDrawingCache(int drawingCacheToKeep) {
        Log.e(TAG, " setPersistentDrawingCache(int drawingCacheToKeep)");
        super.setPersistentDrawingCache(drawingCacheToKeep);
    }

    @Override
    public int getLayoutMode() {
        Log.e(TAG, " getLayoutMode()");
        return super.getLayoutMode();
    }

    @Override
    public void setLayoutMode(int layoutMode) {
        Log.e(TAG, " setLayoutMode(int layoutMode)");
        super.setLayoutMode(layoutMode);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        Log.e(TAG, " generateLayoutParams(AttributeSet attrs)");
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        Log.e(TAG, " generateLayoutParams(LayoutParams p)");
        return super.generateLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        Log.e(TAG, " generateDefaultLayoutParams()");
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected void debug(int depth) {
        Log.e(TAG, " debug(int depth)");
        super.debug(depth);
    }

    @Override
    public int indexOfChild(View child) {
        Log.e(TAG, " indexOfChild(View child)");
        return super.indexOfChild(child);
    }

    @Override
    public int getChildCount() {
        Log.e(TAG, " getChildCount()");
        return super.getChildCount();
    }

    @Override
    public View getChildAt(int index) {
        Log.e(TAG, " getChildAt(int index)");
        return super.getChildAt(index);
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, " measureChildren(int widthMeasureSpec, int heightMeasureSpec)");
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        Log.e(TAG, " measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec)");
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        Log.e(TAG, " measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed)");
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public void clearDisappearingChildren() {
        Log.e(TAG, " clearDisappearingChildren()");
        super.clearDisappearingChildren();
    }

    @Override
    public void startViewTransition(View view) {
        Log.e(TAG, " startViewTransition(View view)");
        super.startViewTransition(view);
    }

    @Override
    public void endViewTransition(View view) {
        Log.e(TAG, " endViewTransition(View view)");
        super.endViewTransition(view);
    }

    @Override
    public void suppressLayout(boolean suppress) {
        Log.e(TAG, " suppressLayout(boolean suppress)");
        super.suppressLayout(suppress);
    }

    @Override
    public boolean isLayoutSuppressed() {
        Log.e(TAG, " isLayoutSuppressed()");
        return super.isLayoutSuppressed();
    }

    @Override
    public boolean gatherTransparentRegion(Region region) {
        Log.e(TAG, " gatherTransparentRegion(Region region)");
        return super.gatherTransparentRegion(region);
    }

    @Override
    public void requestTransparentRegion(View child) {
        Log.e(TAG, " requestTransparentRegion(View child)");
        super.requestTransparentRegion(child);
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        Log.e(TAG, " dispatchApplyWindowInsets(WindowInsets insets)");
        return super.dispatchApplyWindowInsets(insets);
    }

    @Override
    public Animation.AnimationListener getLayoutAnimationListener() {
        Log.e(TAG, " getLayoutAnimationListener()");
        return super.getLayoutAnimationListener();
    }

    @Override
    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
        Log.e(TAG, " setLayoutAnimationListener(Animation.AnimationListener animationListener)");
        super.setLayoutAnimationListener(animationListener);
    }

    @Override
    protected void drawableStateChanged() {
        Log.e(TAG, " drawableStateChanged()");
        super.drawableStateChanged();
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        Log.e(TAG, " jumpDrawablesToCurrentState()");
        super.jumpDrawablesToCurrentState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        Log.e(TAG, " onCreateDrawableState(int extraSpace)");
        return super.onCreateDrawableState(extraSpace);
    }

    @Override
    public void setAddStatesFromChildren(boolean addsStates) {
        Log.e(TAG, " setAddStatesFromChildren(boolean addsStates)");
        super.setAddStatesFromChildren(addsStates);
    }

    @Override
    public boolean addStatesFromChildren() {
        Log.e(TAG, " addStatesFromChildren()");
        return super.addStatesFromChildren();
    }

    @Override
    public void childDrawableStateChanged(View child) {
        Log.e(TAG, " childDrawableStateChanged(View child)");
        super.childDrawableStateChanged(child);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        Log.e(TAG, " shouldDelayChildPressedState()");
        return super.shouldDelayChildPressedState();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, " onStartNestedScroll(View child, View target, int nestedScrollAxes)");
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.e(TAG, " onNestedScrollAccepted(View child, View target, int axes)");
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.e(TAG, " onStopNestedScroll(View child)");
        super.onStopNestedScroll(child);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, " onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)");
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, " onNestedPreScroll(View target, int dx, int dy, int[] consumed)");
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, " onNestedFling(View target, float velocityX, float velocityY, boolean consumed)");
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, " onNestedPreFling(View target, float velocityX, float velocityY)");
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, " getNestedScrollAxes()");
        return super.getNestedScrollAxes();
    }
}