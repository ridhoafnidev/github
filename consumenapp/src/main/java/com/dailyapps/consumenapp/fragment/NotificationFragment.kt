package com.dailyapps.consumenapp.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dailyapps.consumenapp.R
import com.dailyapps.consumenapp.databinding.FragmentNotificationBinding
import com.dailyapps.consumenapp.receiver.AlarmReceiver

class NotificationFragment : Fragment() {
    companion object {
        const val PREFS_NAME = "SettingPref"
        private const val DAILY = "daily"
    }
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        alarmReceiver = AlarmReceiver()
        sharedPreferences = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        (activity as AppCompatActivity).supportActionBar?.setTitle(getString(R.string.setting))
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.swDaily.isChecked = sharedPreferences.getBoolean(DAILY, false)
        binding.swDaily.setOnCheckedChangeListener { _, isCheck ->
            if (isCheck){
                alarmReceiver.setDailyReminder(
                        context,
                        AlarmReceiver.TYPE_DAILY,
                        getString(R.string.daily_message)
                )
            }else{
                alarmReceiver.cancelAlarm(context)
            }
            sharedPreferences.edit {
                putBoolean(DAILY, isCheck)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            val homeFragment = NotificationFragmentDirections.actionNotificationFragmentToFavoriteFragment()
            findNavController().navigate(homeFragment)
        }
        return super.onOptionsItemSelected(item)
    }

}