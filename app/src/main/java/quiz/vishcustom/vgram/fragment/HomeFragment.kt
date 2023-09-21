package quiz.vishcustom.vgram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import quiz.vishcustom.vgram.Models.Post
import quiz.vishcustom.vgram.Models.User
import quiz.vishcustom.vgram.R
import quiz.vishcustom.vgram.adapter.FollowAdapter
import quiz.vishcustom.vgram.adapter.PostAdapter
import quiz.vishcustom.vgram.databinding.FragmentHomeBinding
import quiz.vishcustom.vgram.utils.FOLLOW
import quiz.vishcustom.vgram.utils.POST


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var postList = ArrayList<Post>()
        private lateinit var adapter: PostAdapter
    private var followList = ArrayList<User>()
        private lateinit var followAdapter: FollowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        (requireActivity()as AppCompatActivity).setSupportActionBar(binding.toolbar)
        adapter = PostAdapter(requireContext(),postList)
        binding.postRv.layoutManager=LinearLayoutManager(requireContext())
        binding.postRv.adapter=adapter

        //getting all the follower list on home

        followAdapter = FollowAdapter(requireContext(), followList)
        binding.followRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        binding.followRv.adapter = followAdapter
        setHasOptionsMenu(true)
        (requireActivity()as AppCompatActivity).setSupportActionBar(binding.toolbar)

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).get()
            .addOnSuccessListener {
                var tempList = ArrayList<User>()
                followList.clear()
                for(i in it.documents){
                    var user:User = i.toObject<User>()!!
                    tempList.add(user)
                }
                followList.addAll(tempList)
                followAdapter.notifyDataSetChanged()
            }

        //getting all the post
        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList = ArrayList<Post>()
            postList.clear()
            for(i in it.documents){
                var post:Post = i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}