package com.example.brainaidlserver.activity.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainaidlserver.R
import com.example.brainaidlserver.activity.data.LoginRepository
import com.example.brainaidlserver.activity.data.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    private fun loginFlow() = flow {
        emit(loginRepository.loginWithTaskC())
        emit(loginRepository.loginWithTaskB())
        emit(loginRepository.loginWithTaskA())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loginFlow2() {
        viewModelScope.launch {
            val taskC = async(start = CoroutineStart.LAZY) {
                Log.i("Brain", "begin async coroutine taskC")
                loginRepository.loginWithTaskC()
            }
            val taskB = async { loginRepository.loginWithTaskB() }
            val taskA = async { loginRepository.loginWithTaskA() }

            listOf(taskC, taskA, taskB).map {
                flow { emit(it.await()) }
            }.merge().collect {
                Log.i("Brain", "receive result = $it")
            }
        }

        /*val receiveChannel = viewModelScope.produce {
            async{ send(loginRepository.loginWithTaskC()) }
            async{ send(loginRepository.loginWithTaskB()) }
            async{ send(loginRepository.loginWithTaskA()) }
        }*/
    }


    fun login() {
        GlobalScope
        //loginFlow2()
        runBlocking {
            /*async(context = Dispatchers.IO, start = CoroutineStart.UNDISPATCHED) {
                Log.i("Brain", "begin async  thread = ${Thread.currentThread().name}")
                delay(1000)
                Log.i("Brain", "end async thread = ${Thread.currentThread().name}")
            }*/
            Log.i("Brain", "runBlocking thread = ${Thread.currentThread().name}")
           /* loginFlow().buffer(3).onCompletion {
                Log.i("Brain", "onCompletion $it")
            }.collect {
                Log.i("Brain", "$it")
            }*/
            /*val sum = (2..5).asFlow().map {
                Log.i("Brain", " it * it = ${it * it}")
                it * it
            }.reduce { accumulator, value ->
                Log.i("Brain", "accumulator = $accumulator, value = $value")
                accumulator + value
            }
            Log.i("Brain", "sum = $sum")*/

            /*val nums = (1..10).asFlow().onEach { delay(2000) }
            val strs = flowOf("one", "two", "three").onEach { delay(1000) }
            val startTime = System.currentTimeMillis()*/
            /*strs.combine(nums) { a , b ->
                "$a -> $b"
            }.collect {
                Log.i("Brain", "$it at ${System.currentTimeMillis() - startTime} ms from start")
            }*/
            /*nums.take(2).combine(strs) { a, b ->
                "$a -> $b"
            }.collect {
                Log.i("Brain", "$it at ${System.currentTimeMillis() - startTime} ms from start")
            }*/
            /*strs.zip(nums) { a, b ->
                "$a -> $b"
            }.collect {
                Log.i("Brain", "$it at ${System.currentTimeMillis() - startTime} ms from start")
            }*/

        }

        /*runBlocking {
            val flow = loginFlow()
            flow.collect { data ->
                val taskResult = data.await()
                Log.i("Brain", "Collect data $taskResult")
            }
        }*/
        /*viewModelScope.launch {
            val timing = measureTimeMillis {
                val taskC  = async {
                    loginRepository.loginWithTaskC()
                    //Log.i("Brain", "task c result = $taskCResult")
                }
                val taskA = async {
                    loginRepository.loginWithTaskA()
                    //Log.i("Brain", "task a result = $taskAResult")
                }
                val taskB = async {
                    loginRepository.loginWithTaskB()
                    //Log.i("Brain", "task b result = $taskBResult")
                }
                val taskCResult = taskC.await()
                val taskAResult = taskA.await()
                val taskBResult = taskB.await()
                Log.i("Brain", "task c result = $taskCResult")
                Log.i("Brain", "task a result = $taskAResult")
                Log.i("Brain", "task b result = $taskBResult")
                Log.i("Brain", "end of measureTimeMillis block.")
            }
            Log.i("Brain", "out of async scope timing = $timing")
        }*/

    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}