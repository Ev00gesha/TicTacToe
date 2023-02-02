package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.tictactoe.databinding.ActivityMainBinding
import com.example.tictactoe.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private var currentSound = 0
    private var currentLevel = 0
    private var currentRules = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        val data = getSettingsInfo()

        currentSound = data.sound
        currentLevel = data.level
        currentRules = data.rules

        when(currentRules){
            1 -> binding.checkBoxVertical.isChecked = true
            2 -> binding.checkBoxHorizontal.isChecked = true
            3 -> {
                binding.checkBoxVertical.isChecked = true
                binding.checkBoxHorizontal.isChecked = true
            }
            4 -> binding.checkBoxDiagonal.isChecked = true
            5 -> {
                binding.checkBoxDiagonal.isChecked = true
                binding.checkBoxVertical.isChecked = true
            }
            6 -> {
                binding.checkBoxDiagonal.isChecked = true
                binding.checkBoxHorizontal.isChecked = true
            }
            7 -> {
                binding.checkBoxVertical.isChecked = true
                binding.checkBoxHorizontal.isChecked = true
                binding.checkBoxDiagonal.isChecked = true
            }
        }

        if(currentLevel == 0){
            binding.previousLevel.visibility = View.INVISIBLE
        }else if(currentLevel == 2){
            binding.nextLevel.visibility = View.INVISIBLE
        }

        binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLevel]
        binding.soundBar.progress = currentSound

        binding.toBack.setOnClickListener {
            onBackPressed()
        }

        binding.previousLevel.setOnClickListener {
            currentLevel--
            if(currentLevel == 0){
                binding.previousLevel.visibility = View.INVISIBLE
            }else if(currentLevel == 1){
                binding.nextLevel.visibility = View.VISIBLE
            }
            binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLevel]
            updateLevel(currentLevel)
        }

        binding.nextLevel.setOnClickListener {
            currentLevel++
            if(currentLevel == 1){
                binding.previousLevel.visibility = View.VISIBLE
            }else if(currentLevel == 2){
                binding.nextLevel.visibility = View.INVISIBLE
            }
            binding.infoLevel.text = resources.getStringArray(R.array.game_level)[currentLevel]
            updateLevel(currentLevel)
        }

        binding.soundBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, value: Int, p2: Boolean) {
                currentSound = value
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                updateSoundValue(currentSound)
            }
        })

        binding.checkBoxVertical.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                currentRules++
            }else{
                currentRules--
            }


            updateRules(currentRules)
        }

        binding.checkBoxHorizontal.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                currentRules+=2
            }else{
                currentRules-=2
            }
            updateRules(currentRules)
        }

        binding.checkBoxDiagonal.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                currentRules+=4
            }else{
                currentRules-=4
            }
            updateRules(currentRules)
        }

        setContentView(binding.root)
    }

    private fun updateSoundValue(value: Int){
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()){
            putInt(PREF_SOUND, value)
            apply()
        }
        setResult(RESULT_OK)
    }

    private fun updateLevel(level: Int){
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()){
            putInt(PREF_LEVEL, level)
            apply()
        }
        setResult(RESULT_OK)
    }

    private fun updateRules(rules: Int){
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).edit()){
            putInt(PREF_RULES, rules)
            apply()
        }
        setResult(RESULT_OK)
    }

    private fun getSettingsInfo():InfoSettings{
        with(getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)){
            val sound = getInt(PREF_SOUND, 30)
            val level = getInt(PREF_LEVEL, 0)
            val rules = getInt(PREF_RULES, 7)
            return InfoSettings(sound, level, rules)
        }
    }

    data class InfoSettings(val sound:Int, val level:Int, val rules:Int)

    companion object{
        const val PREF_SOUND = "pref_sound"
        const val PREF_LEVEL = "pref_level"
        const val PREF_RULES = "pref_rules"
    }
}