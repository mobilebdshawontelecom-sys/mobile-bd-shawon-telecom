package com.shawon.mobilebdshawontelecom

import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun setupRoot() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 36, 28, 24)
            setBackgroundColor(0xFFF4F8FF.toInt())
        }
        setContentView(root)
    }

    private fun heading(text: String) {
        root.addView(TextView(this).apply {
            this.text = text
            textSize = 23f
            setTextColor(0xFF123D7A.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 22)
        })
    }

    private fun field(hintText: String, password: Boolean = false): EditText {
        return EditText(this).apply {
            hint = hintText
            textSize = 16f
            if (password) inputType = 129
            root.addView(this, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 12 })
        }
    }

    private fun button(label: String, action: () -> Unit) {
        root.addView(Button(this).apply {
            text = label
            setOnClickListener { action() }
        }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 8 })
    }

    private fun showHome() {
        setupRoot()
        heading("মোবাইল বিডি & শাওন টেলিকম")
        val user = auth.currentUser
        if (user == null) {
            val email = field("Gmail")
            val pass = field("Password", true)
            button("Login") {
                val e = email.text.toString().trim()
                val p = pass.text.toString()
                if (e.isBlank() || p.length < 6) {
                    toast("সঠিক Gmail ও কমপক্ষে ৬ অক্ষরের পাসওয়ার্ড দিন")
                } else auth.signInWithEmailAndPassword(e, p)
                    .addOnSuccessListener { showDashboard() }
                    .addOnFailureListener { toast(it.localizedMessage ?: "Login হয়নি") }
            }
            button("Create account") {
                val e = email.text.toString().trim()
                val p = pass.text.toString()
                if (e.isBlank() || p.length < 6) toast("Gmail ও কমপক্ষে ৬ অক্ষরের পাসওয়ার্ড দিন")
                else auth.createUserWithEmailAndPassword(e, p)
                    .addOnSuccessListener { showDashboard() }
                    .addOnFailureListener { toast(it.localizedMessage ?: "Account তৈরি হয়নি") }
            }
            root.addView(TextView(this).apply {
                text = "প্রাথমিক সংস্করণ: Email/Password লগইন। Google ও ফোন OTP, দোকানের হিসাব এবং স্টক ফিচার পরের ধাপে যুক্ত হবে।"
                textSize = 14f
                setPadding(4, 18, 4, 0)
            })
        } else showDashboard()
    }

    private fun showDashboard() {
        setupRoot()
        heading("মোবাইল বিডি & শাওন টেলিকম")
        root.addView(TextView(this).apply {
            text = "লগইন: ${auth.currentUser?.email ?: ""}\n\nএটি প্রাথমিক অ্যাপ কাঠামো।"
            textSize = 17f
            setTextColor(0xFF24364B.toInt())
            setPadding(0, 16, 0, 20)
        })
        button("Cloud connection test") {
            val uid = auth.currentUser?.uid ?: return@button
            db.collection("users").document(uid).set(
                mapOf("email" to auth.currentUser?.email, "updatedAt" to System.currentTimeMillis())
            ).addOnSuccessListener { toast("Firebase সংযোগ সফল") }
             .addOnFailureListener { toast(it.localizedMessage ?: "সংযোগ হয়নি") }
        }
        button("Logout") {
            auth.signOut()
            showHome()
        }
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
