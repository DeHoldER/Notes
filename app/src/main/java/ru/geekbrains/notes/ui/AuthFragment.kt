package ru.geekbrains.notes.ui

import android.content.Context
import ru.geekbrains.notes.ui.NoteListFragment.Companion.newInstance
import ru.geekbrains.notes.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import ru.geekbrains.notes.MainActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import ru.geekbrains.notes.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import android.content.SharedPreferences
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class AuthFragment : Fragment() {
    private var navigation: Navigation? = null

    // Клиент для регистрации пользователя через Google
    private var googleSignInClient: GoogleSignInClient? = null

    // Кнопка регистрации через Google
    private var buttonSignIn: SignInButton? = null
    private var emailView: TextView? = null
    private var continue_: MaterialButton? = null
    private var mainActivity: MainActivity? = null
    private var userName: String? = null
    private var userEmail: String? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Получим навигацию по приложению, чтобы перейти на фрагмент со списком карточек
        mainActivity = context as MainActivity
        navigation = mainActivity!!.navigation
    }

    override fun onDetach() {
        navigation = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)
        initGoogleSign()
        initView(view)
        enableSign()
        return view
    }

    // Инициализация запроса на аутентификацию
    private fun initGoogleSign() {
// Конфигурация запроса на регистрацию пользователя, чтобы получить
// идентификатор пользователя, его почту и основной профайл
// (регулируется параметром)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Получаем клиента для регистрации и данные по клиенту
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    // Инициализация пользовательских элементов
    private fun initView(view: View) {
// Кнопка регистрации пользователя
        buttonSignIn = view.findViewById(R.id.sign_in_button)
        buttonSignIn?.setOnClickListener(View.OnClickListener { v: View? -> signIn() })
        emailView = view.findViewById(R.id.email)
        // Кнопка «Продолжить», будем показывать главный фрагмент
        continue_ = view.findViewById(R.id.continue_)
        continue_?.setOnClickListener(View.OnClickListener { v: View? ->
            navigation!!.addFragment(
                newInstance(userEmail), false
            )
        })
    }

    override fun onStart() {
        super.onStart()
        // Проверим, входил ли пользователь в это приложение через Google
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
// Пользователь уже входил, сделаем кнопку недоступной
            disableSign()
            // Обновим почтовый адрес этого пользователя и выведем его на экран
            updateUI(account.email)
            userName = account.displayName
            userEmail = account.email
            mainActivity!!.setUserOnMenu(userName, userEmail)
            val sharedPreferences =
                mainActivity!!.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor
            editor = sharedPreferences.edit()
            editor.putString("userEmail", userEmail).apply()
            editor.putString("userName", userName).apply()
        }
    }

    // Инициируем регистрацию пользователя
    private fun signIn() {
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Здесь получим ответ от системы, что пользователь вошёл
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
// Когда сюда возвращается Task, результаты аутентификации уже
// готовы
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // Получаем данные пользователя
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            ) ?: return
            // Регистрация прошла успешно
            disableSign()
            updateUI(account.email)
        } catch (e: ApiException) {
// The ApiException status code indicates the detailed failure
            // reason. Please refer to the GoogleSignInStatusCodes class
// reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    // Обновляем данные о пользователе на экране
    private fun updateUI(email: String) {
        emailView!!.text = email
    }

    // Разрешить аутентификацию и запретить остальные действия
    private fun enableSign() {
        buttonSignIn!!.isEnabled = true
        continue_!!.isEnabled = false
    }

    // Запретить аутентификацию (уже прошла) и разрешить остальные действия
    private fun disableSign() {
        buttonSignIn!!.isEnabled = false
        continue_!!.isEnabled = true
    }

    companion object {
        // Используется, чтобы определить результат activity регистрации через Google
        private const val RC_SIGN_IN = 40404
        private const val TAG = "GoogleAuth"
        fun newInstance(): AuthFragment {
            return AuthFragment()
        }
    }
}