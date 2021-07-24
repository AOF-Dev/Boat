package com.mojang.minecraftpe;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

public class TextInputProxyEditTextbox extends EditText {
    /* access modifiers changed from: private */
    public MCPEKeyWatcher _mcpeKeyWatcher;
    public final int allowedLength;
    public final boolean limitInput;

    public interface MCPEKeyWatcher {
        void onBackKeyPressed();

        void onDeleteKeyPressed();
    }

    public TextInputProxyEditTextbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this._mcpeKeyWatcher = null;
        this.allowedLength = 160;
        this.limitInput = false;
    }

    public TextInputProxyEditTextbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._mcpeKeyWatcher = null;
        this.allowedLength = 160;
        this.limitInput = false;
    }

    public TextInputProxyEditTextbox(Context context, int allowedLength2, boolean limitInput2) {
        super(context);
        this._mcpeKeyWatcher = null;
        this.allowedLength = allowedLength2;
        this.limitInput = limitInput2;
        setFilters(limitInput2 ? new InputFilter[]{new InputFilter.LengthFilter(this.allowedLength), new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals("")) {
                }
                return source;
            }
        }} : new InputFilter[]{new InputFilter.LengthFilter(this.allowedLength)});
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MCPEInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getAction() != 1) {
            return super.onKeyPreIme(keyCode, event);
        }
        if (this._mcpeKeyWatcher != null) {
            this._mcpeKeyWatcher.onBackKeyPressed();
        }
        return false;
    }

    public void setOnMCPEKeyWatcher(MCPEKeyWatcher mcpeKeyWatcher) {
        this._mcpeKeyWatcher = mcpeKeyWatcher;
    }

    private class MCPEInputConnection extends InputConnectionWrapper {
        TextInputProxyEditTextbox textbox;

        public MCPEInputConnection(InputConnection target, boolean mutable, TextInputProxyEditTextbox textbox2) {
            super(target, mutable);
            this.textbox = textbox2;
        }

        public boolean sendKeyEvent(KeyEvent event) {
            if (this.textbox.getText().length() != 0 || event.getAction() != 0 || event.getKeyCode() != 67) {
                return super.sendKeyEvent(event);
            }
            if (TextInputProxyEditTextbox.this._mcpeKeyWatcher != null) {
                TextInputProxyEditTextbox.this._mcpeKeyWatcher.onDeleteKeyPressed();
            }
            return false;
        }
    }
}
