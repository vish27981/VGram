package quiz.vishcustom.vgram.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import quiz.vishcustom.vgram.Models.User
import quiz.vishcustom.vgram.SignUpActivity
import quiz.vishcustom.vgram.adapter.ViewPager
import quiz.vishcustom.vgram.databinding.FragmentProfileBinding
import quiz.vishcustom.vgram.utils.USER_NODE


class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container,false)
        //for editing the profile image
        binding.editProfile.setOnClickListener {
            val intent = Intent(activity,SignUpActivity::class.java)
            intent.putExtra("MODE",1)
            activity?.startActivity(intent)
            activity?.finish()
        }

        viewPager = ViewPager(requireActivity().supportFragmentManager)
        viewPager.addFragments(MyPostFragment(),"POST")
        viewPager.addFragments(MyReelsFragment(),"REELS")
        binding.viewPager.adapter=viewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager)



        return binding.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                binding.name.text= user.name
                binding.bio.text=user.email
                if(!user.image.isNullOrEmpty()){
                    Picasso.get().load(user.image).into(binding.profileImage)
                }

            }
    }
}