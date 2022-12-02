package com.polar.industries.teskotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.polar.industries.teskotlin.fragments.ContractsFinishedFragment
import com.polar.industries.teskotlin.fragments.ContractsRejectedFragment
import com.polar.industries.teskotlin.fragments.ContractsWithoutFinishFragment

class ContractsActivity : AppCompatActivity() {

    private lateinit var viewPager_Contracts: ViewPager
    private lateinit var tabLayout_Contracts: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contracts)
        title = "Contratos"
        //supportActionBar!!.hide()


        viewPager_Contracts = findViewById(R.id.viewPager_Contracts)
        tabLayout_Contracts = findViewById(R.id.tabLayout_Contracts)

        tabLayout_Contracts.setupWithViewPager(viewPager_Contracts)
        setupViewPager(viewPager_Contracts)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.prestador_servicios, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_menu_prestador->{
                val intent: Intent = Intent(this@ContractsActivity, OpcionesActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }


    // This function is used to add items in arraylist and assign
    // the adapter to view pager
    private fun setupViewPager(viewpager: ViewPager) {
        var adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        // LoginFragment is the name of Fragment and the Login
        // is a title of tab
        adapter.addFragment(ContractsWithoutFinishFragment(), "Pendientes")
        adapter.addFragment(ContractsFinishedFragment(), "Finalizados")
        adapter.addFragment(ContractsRejectedFragment(), "Rechazados")

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

}