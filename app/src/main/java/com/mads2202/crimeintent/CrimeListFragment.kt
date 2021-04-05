package com.mads2202.crimeintent

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.*


class CrimeListFragment : Fragment() {
    lateinit var mCrimeRecyclerView: RecyclerView
    lateinit var mCrimeAdapter: CrimeAdapter
    var callback: Callbacks? = null
    var isSubtitleVisible = false
    var mPosition: Int = 0

    companion object {
        val SAVED_SUBTITLE_VISIBLE = "subtitle"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let { CrimeLab.initDB(it) }
        var mView = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = mView.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)
        if (savedInstanceState?.getBoolean(SAVED_SUBTITLE_VISIBLE) != null)
            isSubtitleVisible = savedInstanceState!!.getBoolean(SAVED_SUBTITLE_VISIBLE)
        updateUi()
        return mView
    }

     fun updateUi(): Unit {
        mCrimeAdapter = CrimeAdapter(CrimeLab.getCrimes() as MutableList<Crime>)
        mCrimeRecyclerView.adapter = mCrimeAdapter
        mCrimeAdapter.notifyItemChanged(mPosition)
        mCrimeAdapter.notifyDataSetChanged()
        updateSubtitle()


    }


    inner class CrimeHolder(inflater: LayoutInflater, parent: ViewGroup) : ViewHolder(
        inflater.inflate(R.layout.list_item_crime, parent, false)
    ) {
        var mTitleTextView: TextView
        var mDateTextView: TextView
        var mCrimeSolved: ImageView
        lateinit var mCrime: Crime

        init {
            mTitleTextView = itemView.findViewById(R.id.crime_title)
            mDateTextView = itemView.findViewById(R.id.crime_date)
            mCrimeSolved = itemView.findViewById(R.id.crime_solved)


        }

        fun bind(crime: Crime) {
            if (!crime.mRequiresPolice) {
                mCrime = crime
                mTitleTextView.text = crime.mTitle
                mDateTextView.text = crime.mDate.toString()
            }


        }

    }

    inner class SeriousCrimeHolder(itemView: View) : ViewHolder(itemView) {
        var mTitleTextView: TextView
        var mDateTextView: TextView
        lateinit var mCrime: Crime

        init {
            mTitleTextView = itemView.findViewById(R.id.crime_title)
            mDateTextView = itemView.findViewById(R.id.crime_date)


        }

        fun bind(crime: Crime) {
            if (crime.mRequiresPolice) {
                mCrime = crime
                mTitleTextView.text = crime.mTitle
                mDateTextView.text = DateFormat.format("EEEE, MMMM d, yyyy", crime.mDate)
            }

        }

    }

    inner class CrimeAdapter(var crimeList: List<Crime>) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var inflater: LayoutInflater = LayoutInflater.from(parent.context)
            var view: View
            if (viewType == 0) {
                inflater.inflate(R.layout.list_item_crime, parent, false)
                return CrimeHolder(inflater, parent)
            } else {
                view = inflater.inflate(R.layout.serious_list_item_crime, parent, false)
                return SeriousCrimeHolder(view)
            }
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (!crimeList.get(position).mRequiresPolice) run {
                var crimeHolder: CrimeHolder = holder as CrimeHolder
                holder.bind(crimeList.get(position))
            } else {
                var seriousCrimeHolder: SeriousCrimeHolder = holder as SeriousCrimeHolder
                holder.bind(crimeList.get(position))

            }
            holder.itemView.setOnClickListener {
                callback!!.onCrimeSelected(crimeList[position])
                mPosition = position
            }

        }

        override fun getItemCount(): Int {
            return crimeList.size
        }

        override fun getItemViewType(position: Int): Int {
            if (!crimeList.get(position).mRequiresPolice)
                return 0
            else return 1
        }

        fun setCrimes(crimes: List<Crime>) {
            crimeList = crimes
        }
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
        var menuItem = menu.findItem(R.id.show_subtitle)
        if (isSubtitleVisible) {
            menuItem.setTitle(R.string.hide_subtitle)
        } else {
            menuItem.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime(UUID.randomUUID())
                CrimeLab.addCrime(crime)
                updateUi()
                callback!!.onCrimeSelected(crime)
                return true
            }
            R.id.show_subtitle -> {
                updateSubtitle()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateSubtitle() {
        val crimeCount: Int = CrimeLab.getCrimes()!!.size
        var subtitle: String? = getString(R.string.subtitle_format, crimeCount)
        if (isSubtitleVisible)
            subtitle = null
        val activity = activity as AppCompatActivity?
        activity!!.supportActionBar!!.subtitle = subtitle
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, isSubtitleVisible)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}