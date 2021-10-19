package com.numan.videoeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.numan.videoeditor.R
import com.numan.videoeditor.db.BannerData
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder


class BannerAdapterAdapter : BaseBannerAdapter<BannerData?, BannerAdapterAdapter.NetViewHolder>() {
    override fun onBind(holder: NetViewHolder, data: BannerData?, position: Int, pageSize: Int) {
        holder.bindData(data, position, pageSize)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_row
    }

    override fun createViewHolder(
        parent: ViewGroup,
        itemView: View?,
        viewType: Int
    ): NetViewHolder {
        return NetViewHolder(itemView)
    }

    class NetViewHolder(itemView: View?) : BaseViewHolder<BannerData?>(itemView!!) {

        override fun bindData(data: BannerData?, position: Int, pageSize: Int) {
            var imageViewBackground: ImageView = itemView.findViewById(R.id.banner_iv)
            Glide.with(itemView).load(data?.imageBanner).placeholder(R.drawable.shadow)
                .into(imageViewBackground)
        }

    }
}
