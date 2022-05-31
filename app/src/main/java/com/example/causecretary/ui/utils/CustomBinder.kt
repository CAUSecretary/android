package com.example.causecretary.ui.utils
import android.text.TextUtils
import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.TextView
import com.example.causecretary.ui.data.Consts
import com.example.causecretary.R


class CustomBinder {

    companion object {
        @JvmStatic
        @BindingAdapter("visibleGone")
        fun setVisibility(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.GONE
        }

        @JvmStatic
        @BindingAdapter("visibleInvisible")
        fun setVisibility2(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }

//        @JvmStatic
//        @BindingAdapter("toolbarText")
//        fun setToolbarTitle(textView: TextView, title: String) {
//            textView.visibility = View.GONE
//            textView.post {
//                if (!TextUtils.isEmpty(title)) {
//                    textView.visibility = View.VISIBLE
//                }
//            }
//
//            textView.text = title
//        }

//        @JvmStatic
//        @BindingAdapter("termsTitle")
//        fun setTermsTitle(textView: TextView, termsType: Consts.TermsType?) {
//            when(termsType) {
//                Consts.TermsType.TERMS_USE -> {
//                    textView.text = textView.context.getText(R.string.join_terms_use_title)
//                }
//                Consts.TermsType.TERMS_PERSONAL -> {
//                    textView.text = textView.context.getText(R.string.join_terms_personal_title)
//                }
//                Consts.TermsType.TERMS_UNIQUE -> {
//                    textView.text = textView.context.getText(R.string.join_terms_unique_title)
//                }
//                else -> {
//                    textView.text = ""
//                }
//            }
//        }

//        @JvmStatic
//        @BindingAdapter("termsContents")
//        fun setTermsContents(textView: TextView, termsType: Consts.TermsType?) {
//            when(termsType) {
//                Consts.TermsType.TERMS_USE -> {
//                    textView.text = textView.context.getText(R.string.join_terms_use_contents)
//                }
//                Consts.TermsType.TERMS_PERSONAL -> {
//                    textView.text = textView.context.getText(R.string.join_terms_personal_contents)
//                }
//                Consts.TermsType.TERMS_UNIQUE -> {
//                    textView.text=textView.context.getText(R.string.join_terms_unique_contents)
//                }
//                else -> {
//                    textView.text = ""
//                }
//            }
//        }
    }
}