package ba.etf.rma21.projekat



import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.data.fragment.FragmentKvizovi
import ba.etf.rma21.projekat.data.fragment.FragmentPredmeti
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.viewmodel.GroupViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// probni commit za spiralu4
// {"message":"Uspješno ste kreirali account!","link":"http://rma21-etf.herokuapp.com/account/0934422d-53e3-4817-b211-1964211c912d"}

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var bottomNavigation: BottomNavigationView
    }
    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.kvizovi -> {
                        val kvizoviFragments = FragmentKvizovi.newInstance()
                        openFragment(kvizoviFragments)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.predmeti -> {
                        val predmetiFragments = FragmentPredmeti.newInstance()
                        openFragment(predmetiFragments)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

    private val groupViewModel = GroupViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation= findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
        bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
//Defaultni fragment
        bottomNavigation.selectedItemId= R.id.kvizovi
        val kvizoviFragment = FragmentKvizovi.newInstance()
        openFragment(kvizoviFragment)

        val acc = AccountRepository()
        acc.setContext(applicationContext)
        // NOVO
        val payload = intent?.getStringExtra("payload")
        if (payload != null) {
             groupViewModel.promijeniHash(payload, onSuccess = ::onSuccess, onError = ::onError)
        }
        else groupViewModel.promijeniHash("0934422d-53e3-4817-b211-1964211c912d", onSuccess = ::onSuccess, onError = ::onError)
        // NOVO
    }

    //Funkcija za izmjenu fragmenta
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Handler().postDelayed({
            napraviBottomNav()
            val favoritesFragment = FragmentKvizovi.newInstance()
            openFragment(favoritesFragment)
        }, 2)

    }

    private fun napraviBottomNav() {
        bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = true
        bottomNavigation.menu.findItem(R.id.predmeti).isVisible = true
        bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
        bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
    }

    fun onSuccess(){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(applicationContext, "Sve ok", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(applicationContext, "Neki error", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}

