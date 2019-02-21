package com.criptext.monkeykitui.info.holder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.criptext.monkeykitui.R
import com.criptext.monkeykitui.cav.EmojiHandler
import com.criptext.monkeykitui.util.Utils
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by hirobreak on 04/10/16.
 */

open class InfoHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder {

    val nameTextView: TextView?
    val secondaryTextView: TextView?
    val tagTextView: TextView?
    val avatarImageView: CircleImageView?

    constructor(view : View, textMaxWidth: Int) : super(Utils.getViewWithRecyclerLayoutParams(view)) {
        nameTextView = view.findViewById(R.id.info_name)
        secondaryTextView = view.findViewById(R.id.info_secondary_txt)
        tagTextView = view.findViewById(R.id.info_rol)
        avatarImageView = view.findViewById(R.id.info_avatar)

    }

    open fun setName(name: String){
        nameTextView!!.text = (EmojiHandler.decodeJava(EmojiHandler.decodeJava(name)))
    }

    open fun setSecondaryText(text: String){
        if(text.toLowerCase().equals("online")){
            var color =  secondaryTextView?.context?.resources?.getColor(R.color.mk_status_connected)
            if(color == null){
                color = Color.GREEN
            }
            secondaryTextView?.setTextColor(color)
        }else{
            secondaryTextView?.setTextColor(Color.GRAY)
        }
        secondaryTextView!!.text = text
    }

    open fun setTag(tag: String){
        tagTextView!!.text = tag
    }

    open fun setAvatar(filepath: String?, isGroup: Boolean){
        val imageView = avatarImageView
        if(imageView != null) {
            if (filepath != null && filepath.length > 0)
                Utils.setAvatarAsync(imageView.context, imageView, filepath, !isGroup, null)
            else
                imageView.setImageResource(if (isGroup) R.drawable.mk_default_group_avatar else
                    R.drawable.mk_default_user_img)
        }
    }

    class EndHolder(view: View) : InfoHolder(view, 0);
}


