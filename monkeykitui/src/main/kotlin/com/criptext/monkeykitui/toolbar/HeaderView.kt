package com.criptext.monkeykitui.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.criptext.monkeykitui.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by hirobreak on 10/10/16.
 */
class HeaderView(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs){

    //var firstContainer : LinearLayout
    var secondContainer : LinearLayout
    var imageView : CircleImageView
    var title : TextView
    var subtitle : TextView

    init{
        View.inflate(context, R.layout.custom_toolbar, this)
        //firstContainer = findViewById(R.id.back_button) as LinearLayout
        secondContainer = findViewById(R.id.layoutNameStatus)
        imageView = findViewById(R.id.imageViewAvatar)
        title = findViewById(R.id.textViewTitle)
        subtitle = findViewById(R.id.textViewSubTitle)

    }



}