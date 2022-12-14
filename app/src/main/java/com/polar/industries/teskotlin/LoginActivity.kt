package com.polar.industries.teskotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

import com.polar.industries.teskotlin.fragments.LoginFragment
import com.polar.industries.teskotlin.fragments.SignInFragment
import com.polar.industries.teskotlin.helpers.*
import com.polar.industries.teskotlin.interfaces.Information
import java.util.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), Information {
    private var materialButton_Registrarse_Login: MaterialButton? = null
    private var materialButton_IniciarSesion: MaterialButton? = null
    private var materialButton_OlvidarPass: MaterialButton? = null
    private var textInputLayout_Email: TextInputLayout? = null
    private var textInputLayout_Pass: TextInputLayout? = null
    private val firebaseAuthHelper: FirebaseAuthHelper = FirebaseAuthHelper()
    private val firestoreHelper: FirebaseFirestoreHelper = FirebaseFirestoreHelper()

    private lateinit var  viewPager_Login: ViewPager
    private lateinit var tabLayout_Login: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        firebaseAuthHelper.setContext(this@LoginActivity)
        firebaseAuthHelper.setOnInformationListener(this)
        /*materialButton_Registrarse_Login = findViewById(R.id.materialButton_Registrarse_Login)
        materialButton_IniciarSesion = findViewById(R.id.materialButton_IniciarSesion)
        materialButton_OlvidarPass = findViewById(R.id.materialButton_OlvidarPass)
        textInputLayout_Email = findViewById(R.id.textInputLayout_Email_Login)
        textInputLayout_Pass = findViewById(R.id.textInputLayout_Pass_Login)
        buttons()*/
        viewPager_Login = findViewById(R.id.viewPager_Contracts)
        tabLayout_Login = findViewById(R.id.tabLayout_Contracts)

        tabLayout_Login.setupWithViewPager(viewPager_Login)
        setupViewPager(viewPager_Login)
    }

    private fun setupViewPager(viewpager: ViewPager) {
        var adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        // LoginFragment is the name of Fragment and the Login
        // is a title of tab
        adapter.addFragment(LoginFragment(), "Iniciar Sesi??n")
        adapter.addFragment(SignInFragment(), "Registrarse")

        // setting adapter to view pager.
        viewpager.adapter = adapter
    }

    // This "ViewPagerAdapter" class overrides functions which are
    // necessary to get information about which item is selected
    // by user, what is title for selected item and so on.*/
    class ViewPagerAdapter : FragmentPagerAdapter {

        // objects of arraylist. One is of Fragment type and
        // another one is of String type.*/
        private final var fragmentList1: ArrayList<Fragment> = ArrayList()
        private final var fragmentTitleList1: ArrayList<String> = ArrayList()

        // this is a secondary constructor of ViewPagerAdapter class.
        public constructor(supportFragmentManager: FragmentManager)
                : super(supportFragmentManager)

        // returns which item is selected from arraylist of fragments.
        override fun getItem(position: Int): Fragment {
            return fragmentList1.get(position)
        }

        // returns which item is selected from arraylist of titles.
        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList1.get(position)
        }

        // returns the number of items present in arraylist.
        override fun getCount(): Int {
            return fragmentList1.size
        }

        // this function adds the fragment and title in 2 separate  arraylist.
        fun addFragment(fragment: Fragment, title: String) {
            fragmentList1.add(fragment)
            fragmentTitleList1.add(title)
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuthHelper.currentUser != null) {
            val dialog = ProgressDialog.show(this@LoginActivity, "", "Ingresando... ", true)
            dialog.show()
            firestoreHelper.getData(
                FirebaseAuthHelper!!.currentUser!!.getUid(),
                dialog,
                this@LoginActivity,
                this@LoginActivity
            )
        }
    }

    private fun buttons() {
        materialButton_Registrarse_Login!!.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Registrar usuario", Toast.LENGTH_SHORT).show()
            showDialogDecision()
        }
        materialButton_IniciarSesion!!.setOnClickListener {
            val email = textInputLayout_Email!!.editText!!.text.toString()
            val pass = textInputLayout_Pass!!.editText!!.text.toString()
            if (validarCampos(email, pass)) {
                val dialog = ProgressDialog.show(this@LoginActivity, "", "Ingresando... ", true)
                dialog.show()
                firebaseAuthHelper.signInWithEmailAndPassword(email, pass, dialog)
            }
        }
        materialButton_OlvidarPass!!.setOnClickListener { showDialogRecoverPass() }
    }

    private fun showDialogDecision() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_decision, null)
        builder.setView(view)
        val dialogDEC = builder.create()
        //dialogDEC.setCancelable(false);
        dialogDEC.show()
        val materialButton_desc_cliente =
            dialogDEC.findViewById<MaterialButton>(R.id.materialButton_desc_cliente)
        val materialButton_desc_talachero =
            dialogDEC.findViewById<MaterialButton>(R.id.materialButton_desc_talachero)
        materialButton_desc_cliente.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignInActivity::class.java)
            intent.putExtra("ROL", "CLIENTE")
            startActivity(intent)
            //finish();
        }
        materialButton_desc_talachero.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignInActivity::class.java)
            intent.putExtra("ROL", "TALACHERO")
            startActivity(intent)
            //finish();
        }
    }

    override fun getMessage(message: String?) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun validarCampos(email: String, pass: String): Boolean {
        textInputLayout_Email!!.error = null
        textInputLayout_Pass!!.error = null
        var bEmail = false
        var bPass = false
        if (!email.isEmpty()) {
            if (Pattern.matches(Constantes.EXREGEMAIL, email)) {
                bEmail = true
            } else {
                textInputLayout_Email!!.error = "Correo no v??lido"
            }
        } else {
            textInputLayout_Email!!.error = "Campo requerido"
        }
        if (!pass.isEmpty()) {
            if (pass.length > 5) {
                bPass = true
            } else {
                textInputLayout_Pass!!.error = "La contrase??a debe tener m??nimo 6 caracteres"
            }
        } else {
            textInputLayout_Pass!!.error = "Campo requerido"
        }
        return if (bEmail && bPass) {
            true
        } else {
            false
        }
    }

    private fun showDialogRecoverPass() {
        val builder = AlertDialog.Builder(this@LoginActivity)
        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_recover_pass, null)
        builder.setView(view)
            .setTitle("Recuperar contrase??a")
            .setMessage("Para recuperar tu contrase??a ingresa el correo electr??nico con el que te registraste y da clic en enviar.")
        val dialogRecoverPass = builder.create()
        dialogRecoverPass.setCancelable(false)
        dialogRecoverPass.show()
        val editText_email_recover =
            dialogRecoverPass.findViewById<TextInputLayout>(R.id.editText_email_recover)
        val button_Send_recover =
            dialogRecoverPass.findViewById<MaterialButton>(R.id.button_Send_recover)
        val button_Cancel_recover =
            dialogRecoverPass.findViewById<MaterialButton>(R.id.button_Cancel_recover)
        button_Send_recover.setOnClickListener {
            var flag_Email = false
            if (!editText_email_recover.editText!!.text.toString().isEmpty()) {
                if (StringHelper().isEmail(editText_email_recover.editText!!.text.toString())) {
                    flag_Email = true
                } else {
                    editText_email_recover.error = "Correo electr??nico inv??lido"
                }
            } else {
                editText_email_recover.error = "Correo electr??nico requerido"
            }
            if (flag_Email) {
                FirebaseQueryHelper().BuscarCredenciales(
                    editText_email_recover.editText!!.text.toString(),
                    this@LoginActivity
                )
                dialogRecoverPass.dismiss()
            }
        }
        button_Cancel_recover.setOnClickListener { dialogRecoverPass.dismiss() }
    }
}