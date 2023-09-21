package quiz.vishcustom.vgram.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import quiz.vishcustom.vgram.Post.PostActivity
import quiz.vishcustom.vgram.Post.ReelsActivity
import quiz.vishcustom.vgram.databinding.FragmentAddBinding

class ModelBottomSheet: BottomSheetDialogFragment(){

    private lateinit var binding: FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddBinding.inflate(inflater, container, false)

        //binding the post activity
        binding.post.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),PostActivity::class.java))
            activity?.finish()
        }

        //binding the reels activity

        binding.reel.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),ReelsActivity::class.java))
        }
        return binding.root
    }

    companion object {

    }
}


