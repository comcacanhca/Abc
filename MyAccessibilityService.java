package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;


public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("QuetNode", "Running with delay");
                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                    if (rootNode != null) {

                        Log.d("MyAccessibilityService", "Content Description" ); // In giá trị nodeText

                        findAndClickSendButton(rootNode);
                    } else {
                        Log.d("QuetNode", "Root Node is null after delay");
                    }
                }
            }, 1000); // Độ trễ 500ms (có thể điều chỉnh)
        }
    }
    private void findAndClickSendButton(AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }
        Log.d("QuetNode", "Quet");
        if (node.getContentDescription() != null && node.getContentDescription().toString().equalsIgnoreCase("Send MMS")) {
            if (node.isClickable()) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.d(TAG, "Clicked Send Button");
                return; // Chỉ click một lần cho mỗi cửa sổ
            }
        } else if (node.getText() != null && node.getText().equals("Send MMS")) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d(TAG, "Clicked Send Button");
            return; // Chỉ click một lần cho mỗi cửa sổ
        } else if (node.getViewIdResourceName() != null && node.getViewIdResourceName().equals("Compose:Draft:Send")) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d(TAG, "Clicked Send Button");
            return; // Chỉ click một lần cho mỗi cửa sổ
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            findAndClickSendButton(node.getChild(i));
        }
    }

    private void performTouchFromXml() {
        // Code để đọc XML và thực hiện thao tác chạm
        try {
            // Ví dụ: Đọc tọa độ từ XML (bạn cần tự định nghĩa định dạng XML)
            int x = 500; // Thay bằng giá trị đọc từ XML
            int y = 1000; // Thay bằng giá trị đọc từ XML

            Path path = new Path();
            path.moveTo(x, y);

            GestureDescription.Builder builder = new GestureDescription.Builder();
            GestureDescription gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 100)).build();

            dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    super.onCompleted(gestureDescription);
                    Log.d(TAG, "Gesture completed");
                }

                @Override
                public void onCancelled(GestureDescription gestureDescription) {
                    super.onCancelled(gestureDescription);
                    Log.d(TAG, "Gesture cancelled");
                }
            }, null);

        } catch (Exception e) {
            Log.e(TAG, "Error performing touch: " + e.getMessage());
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
    }
}