package quiz.vishcustom.vgram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import quiz.vishcustom.vgram.Models.Reel
import quiz.vishcustom.vgram.R
import quiz.vishcustom.vgram.databinding.ReelDesignBinding

class ReelAdapter(var context: Context, var reelList: ArrayList<Reel>)
    :RecyclerView.Adapter<ReelAdapter.ViewHolder>() {

    inner class ViewHolder(var binding : ReelDesignBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    var binding = ReelDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        //getting the reel size
        return reelList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getting profile image on the reel
        Picasso.get().load(reelList.get(position).profileLink).placeholder(R.drawable.user).into(holder.binding.profileImage)
        //getting the caption on reel
        holder.binding.caption.setText(reelList.get(position).caption)
        //getting the reel path
        holder.binding.videoView.setVideoPath(reelList.get(position).reelUrl)
        //getting the reel start to play
        holder.binding.videoView.setOnPreparedListener {
            holder.binding.progressBar.visibility = View.GONE
            holder.binding.videoView.start()
        }
    }
}