package com.example.causecretary.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager

class CustomDialog(context: Context, view: View, width: Int = WindowManager.LayoutParams.WRAP_CONTENT, height: Int = WindowManager.LayoutParams.WRAP_CONTENT): Dialog(context) {
    private var mListener: DialogClickListener? = null
    private var mIsClickConfirm: Boolean = false

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(false)
        setContentView(view)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(width, height)
        }

        setOnDismissListener {
            if (!mIsClickConfirm) mListener?.onClose()
        }
    }

    interface DialogClickListener {
        fun onClose()
        fun onConfirm()
    }

    fun setClickListener(listener: DialogClickListener?) {
        mListener = listener
    }

    fun onClose() {
        dismiss()
    }

    fun onConfirm() {
        mIsClickConfirm = true
        dismiss()
        mListener?.onConfirm()
    }
}