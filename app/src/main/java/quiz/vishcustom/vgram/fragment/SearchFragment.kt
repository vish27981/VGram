package quiz.vishcustom.vgram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import quiz.vishcustom.vgram.Models.User
import quiz.vishcustom.vgram.adapter.SearchAdapter
import quiz.vishcustom.vgram.databinding.FragmentSearchBinding
import quiz.vishcustom.vgram.utils.USER_NODE


class SearchFragment : Fragment() {

    lateinit var binding:FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        binding.rv.layoutManager=LinearLayoutManager(requireContext())
        adapter= SearchAdapter(requireContext(),userList)
        binding.rv.adapter = adapter
        //hinding the current user from the follow list
        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            var tempList = ArrayList<User>()
            userList.clear()
            for(i in it.documents){
                if(i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())){

                }
                else{
                    var user:User = i.toObject<User>()!!
                    tempList.add(user)
                }

            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        //serching the user and getting the searched user
        binding.searchButton.setOnClickListener{
            var text = binding.searchView.text.toString()

            Firebase.firestore.collection(USER_NODE).whereEqualTo("name",text).get().addOnSuccessListener {
                var tempList = ArrayList<User>()
                userList.clear()

                // when user enter not existing username
                if(it.isEmpty){

                }else{
                    for(i in it.documents){
                        if(i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())){

                        }
                        else{
                            var user:User = i.toObject<User>()!!
                            tempList.add(user)
                        }

                    }
                    userList.addAll(tempList)
                    adapter.notifyDataSetChanged()

                }

            }

        }
        return binding.root
    }

    companion object {

    }
}