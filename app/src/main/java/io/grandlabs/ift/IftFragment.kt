package io.grandlabs.ift

import android.app.Activity
import android.support.v4.app.Fragment


abstract class IftFragment : Fragment() {
    protected var listener: OnFragmentInteractionListener? = null

    abstract fun getActionBarTitle(): String

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            listener = activity as OnFragmentInteractionListener?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun setCurrentlySelectedFragment(fragment: IftFragment)
    }
}