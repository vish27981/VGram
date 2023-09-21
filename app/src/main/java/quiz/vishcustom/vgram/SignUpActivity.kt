package quiz.vishcustom.vgram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import quiz.vishcustom.vgram.Models.User
import quiz.vishcustom.vgram.utils.USER_NODE
import quiz.vishcustom.vgram.utils.USER_PROFILE_FOLDER
import quiz.vishcustom.vgram.utils.uploadImage
import quiz.vishcustom.vgram.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    val binding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }
lateinit var user: User
private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
    uri?.let {
        uploadImage(uri, USER_PROFILE_FOLDER) {
            if (it!= null) {
                user.image = it
                binding.profileImage.setImageURI(uri)

            }
        }
    }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val text ="<font color=#B0ADAD>Already have an account?</font> <font color=#1E88E5>Login</font>"
        binding.login.setText(Html.fromHtml(text))
        user = User()

        //uploading the profile image
        if (intent.hasExtra("MODE")){

            if(intent.getIntExtra("MODE",-1)==1){

                binding.signUpBtn.text="Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        user  = it.toObject<User>()!!
                        if(!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(binding.profileImage)
                        }
                        binding.name.editText?.setText(user.name)
                        binding.email.editText?.setText(user.email)
                        binding.pass.editText?.setText(user.password)

                    }
            }
        }

        binding.signUpBtn.setOnClickListener{

            if(intent.hasExtra("MODE")){
                if(intent.getIntExtra("MODE",-1)==1){
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@SignUpActivity,HomeActivity::class.java))
                            finish()
                        }

                }
            }
                else {

                if (binding.name.editText?.text.toString().equals("") or
                    binding.email.editText?.text.toString().equals("") or
                    binding.pass.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please fill the all information", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.pass.editText?.text.toString()
                    ).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.name.editText?.text.toString()
                            user.email = binding.email.editText?.text.toString()
                            user.password = binding.pass.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity, HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        binding.addImage.setOnClickListener{
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener{
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()

        }
    }
}