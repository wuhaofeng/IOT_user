package com.iot.user.ui.view.dev;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.iot.user.R;

public class InputDialog {
    private Dialog dialog;
    private Context context;
    private AppCompatTextView titleTextView;
    private AppCompatTextView cancelTextView;
    private AppCompatTextView confirmTextView;
    private AppCompatEditText contentEditText;

    public InputDialog(Context context, String title, String content,String hint) {
        this.context = context;
        init(title, content,hint);
    }

    public InputDialog(Context context, int title, int content,int hint) {
        this.context = context;
        init(context.getString(title), context.getString(content),context.getString(hint));
    }

    public void setCancelListerner(View.OnClickListener listerner) {

        cancelTextView.setOnClickListener(listerner);

    }

    public void setConfirmListerner(View.OnClickListener listerner) {

        confirmTextView.setOnClickListener(listerner);

    }

    public String getTitle() {

        return titleTextView.getText().toString();

    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public String getContent() {

        return contentEditText.getText().toString();

    }

    public void setContent(String content) {

        contentEditText.setText(content);

    }

    private void setContentHint(String hint){
        contentEditText.setHint(hint);
    }

    public void dismiss() {

        dialog.dismiss();

    }

    private void init(String title, String content,String hint) {

        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_input);
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        Window window = dialog.getWindow();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        layoutParams.width = (int) (displayMetrics.widthPixels * 1);
        window.setAttributes(layoutParams);
        titleTextView = (AppCompatTextView)dialog.findViewById(R.id.titleTextView);
        contentEditText = (AppCompatEditText)dialog.findViewById(R.id.contentEditText);
        cancelTextView = (AppCompatTextView)dialog.findViewById(R.id.cancelTextView);
        confirmTextView = (AppCompatTextView)dialog.findViewById(R.id.confirmTextView);
        titleTextView.setText(title);
        contentEditText.setText(content);
        contentEditText.setHint(hint);
        contentEditText.setSelection(content.length());
        contentEditText.post(new Runnable() {
            @Override
            public void run() {
//                InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

    }

    public void show() {
//        Activity activity = dialog.getOwnerActivity();
//        if ( activity != null && !activity.isFinishing() ) {
        try {
            dialog.show();
        }catch (Exception e){

        }
//        }
    }


}
