package quiz.vishcustom.vgram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import quiz.vishcustom.vgram.Models.Post
import quiz.vishcustom.vgram.Models.Reel
import quiz.vishcustom.vgram.R
import quiz.vishcustom.vgram.adapter.MyPostRvAdapter
import quiz.vishcustom.vgram.adapter.MyReelAdapter
import quiz.vishcustom.vgram.databinding.FragmentMyPostBinding
import quiz.vishcustom.vgram.databinding.FragmentMyReelsBinding
import quiz.vishcustom.vgram.utils.REEL


class MyReelsFragment : Fragment() {
    private lateinit var binding: FragmentMyReelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyReelsBinding.inflate(inflater, container, false)

        var reelList = ArrayList<Reel>()
        var adapter = MyReelAdapter(requireContext(),reelList)
        binding.rv.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).get().addOnSuccessListener {
            var tempList = arrayListOf<Reel>()
            for(i in it.documents){
                var reel: Reel =i.toObject<Reel>()!!
                tempList.add(reel)
            }
            reelList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    companion object {

    }
}