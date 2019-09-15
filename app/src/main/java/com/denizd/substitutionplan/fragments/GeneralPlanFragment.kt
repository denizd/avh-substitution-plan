package com.denizd.substitutionplan.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.denizd.substitutionplan.models.Subst

internal class GeneralPlanFragment : PlanFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        substViewModel.allSubst?.observe(this, Observer<List<Subst>> { substitutions ->
            mAdapter.setSubst(substitutions)
            recyclerView.scheduleLayoutAnimation()
        })
    }
}