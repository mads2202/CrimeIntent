package com.mads2202.crimeintent

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CrimeListFragment:Fragment() {
    lateinit var  mCrimeRecyclerView:RecyclerView
    lateinit var mCrimeAdapter:CrimeAdapter
     var mPosition:Int=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView= inflater.inflate(R.layout.fragment_crime_list,container,false)
        mCrimeRecyclerView=mView.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager=LinearLayoutManager(activity)
        updateUi()
        return mView
    }
    private fun updateUi():Unit{
        CrimeLab.fillCrimeList()

        mCrimeAdapter=CrimeAdapter(CrimeLab.mCrimeList)
        mCrimeRecyclerView.adapter=mCrimeAdapter
        mCrimeAdapter.notifyItemChanged(mPosition)


    }


   inner class CrimeHolder(inflater:LayoutInflater,parent:ViewGroup): ViewHolder(
        inflater.inflate(R.layout.list_item_crime,parent,false)){
        var mTitleTextView:TextView
        var mDateTextView:TextView
        var mCrimeSolved:ImageView
        lateinit var mCrime:Crime
       init {
           mTitleTextView=itemView.findViewById(R.id.crime_title)
           mDateTextView=itemView.findViewById(R.id.crime_date)
           mCrimeSolved=itemView.findViewById(R.id.crime_solved)



       }
       fun bind(crime:Crime){
           if(!crime.mRequiresPolice){
           mCrime=crime
           mTitleTextView.text=crime.mTitle
           mDateTextView.text=crime.mDate.toString()
           }


       }

   }
    inner class SeriousCrimeHolder(itemView: View): ViewHolder(itemView){
        var mTitleTextView:TextView
        var mDateTextView:TextView
        lateinit var mCrime:Crime
        init {
            mTitleTextView=itemView.findViewById(R.id.crime_title)
            mDateTextView=itemView.findViewById(R.id.crime_date)



        }
        fun bind(crime:Crime){
            if(crime.mRequiresPolice){
            mCrime=crime
            mTitleTextView.text=crime.mTitle
            mDateTextView.text=DateFormat.format("EEEE, MMMM d, yyyy",crime.mDate)
            }

        }

    }

    inner class CrimeAdapter(var crimeList:List<Crime>):RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var inflater:LayoutInflater= LayoutInflater.from(parent.context)
            var view:View
            if(viewType==0){
                inflater.inflate(R.layout.list_item_crime,parent,false)
                return CrimeHolder(inflater,parent)
            } else{
                view=inflater.inflate(R.layout.serious_list_item_crime,parent,false)
            return SeriousCrimeHolder(view)}
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if(!crimeList.get(position).mRequiresPolice) run {
                var crimeHolder: CrimeHolder = holder as CrimeHolder
                holder.bind(crimeList.get(position))
            } else {
                var seriousCrimeHolder: SeriousCrimeHolder = holder as SeriousCrimeHolder
                holder.bind(crimeList.get(position))

            }
            holder.itemView.setOnClickListener{
                var intent=CrimePaperActivity.newIntent(activity!!,crimeList[position].mId!!)
                startActivity(intent)
                mPosition=position
            }

        }

        override fun getItemCount(): Int {
            return crimeList.size
        }

        override fun getItemViewType(position: Int): Int {
            if(!crimeList.get(position).mRequiresPolice)
                return 0
            else return 1
        }
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }
}