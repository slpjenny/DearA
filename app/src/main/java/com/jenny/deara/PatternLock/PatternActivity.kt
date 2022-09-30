package com.jenny.deara.PatternLock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.jenny.deara.R
import com.jenny.deara.componet.PatternViewStageState
import com.jenny.deara.componet.PatternViewState
import com.jenny.deara.viewmodel.PatternLockViewModel
import kotlinx.android.synthetic.main.activity_pattern.*

class PatternActivity : AppCompatActivity() {
    companion object {
        const val KEY_PATTERN_TYPE = "type"
        const val TYPE_SECURE_MODE = 0
    }

    private val viewModel by viewModels<PatternLockViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)

        clear_TextButton.setOnClickListener {
            viewModel.updateViewState(PatternViewState.Initial)
        }

        stage_Button.setOnClickListener {
            when (pattern_LockView.stageState) {
                PatternViewStageState.FIRST -> {
                    viewModel.updateViewState(PatternViewState.Initial)

                    pattern_LockView.stageState = PatternViewStageState.SECOND
                    stage_Button.text = getString(R.string.stage_button_confirm)
                }
                PatternViewStageState.SECOND -> {
                    AlertDialog.Builder(this).apply {
                        setMessage(R.string.alert_dialog_confirm_message)
                        setPositiveButton(
                            R.string.alert_dialog_positive_button
                        ) { _, _ -> }
                    }.show()
                }
            }
        }

        pattern_LockView.setOnChangeStateListener { state ->
            viewModel.updateViewState(state)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.viewState.collect { patternViewState ->
                when (patternViewState) {
                    is PatternViewState.Initial -> {
                        pattern_LockView.reset()
                        stage_Button.isEnabled = false
                        clear_TextButton.isVisible = false
                        tvMessage.run {
                            text = if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
                                getString(R.string.initial_message_first_stage)
                            } else {
                                getString(R.string.initial_message_second_stage)
                            }
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.message_text_default_color
                                )
                            )
                        }
                    }
                    is PatternViewState.Started -> {
                        tvMessage.run {
                            text = getString(R.string.started_message)
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.message_text_default_color
                                )
                            )
                        }
                    }
                    is PatternViewState.Success -> {
                        stage_Button.isEnabled = true
                        tvMessage.run {
                            text = if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
                                getString(R.string.success_message_first_stage)
                            } else {
                                getString(R.string.success_message_second_stage)
                            }
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.message_text_default_color
                                )
                            )
                        }
                        clear_TextButton.isVisible =
                            pattern_LockView.stageState == PatternViewStageState.FIRST
                    }
                    is PatternViewState.Error -> {
                        tvMessage.run {
                            text = if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
                                getString(R.string.error_message_first_stage)
                            } else {
                                getString(R.string.error_message_second_stage)
                            }
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.message_text_error_color
                                )
                            )
                        }
                        clear_TextButton.isVisible =
                            pattern_LockView.stageState == PatternViewStageState.FIRST
                    }
                }
            }
        }
    }
}
/*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jenny.deara.R
import kotlinx.android.synthetic.main.activity_pattern.*
import kotlinx.android.synthetic.main.activity_setting_lock.*

class PatternActivity : AppCompatActivity() {

    companion object {
        const val KEY_PATTERN_TYPE = "type"
        const val TYPE_DEFAULT = 0
        */
/*const val TYPE_WITH_INDICATOR = 1
        const val TYPE_JD_STYLE = 2
        const val TYPE_9x9 = 3
        const val TYPE_SECURE_MODE = 4*//*

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)

        pattern_arrow.setOnClickListener { _-> startPatternActivity(TYPE_DEFAULT) }

        */
/*defaultBtn.setOnClickListener { _-> startPatternActivity(TYPE_DEFAULT) }
        jdStyleBtn.setOnClickListener { _-> startPatternActivity(TYPE_JD_STYLE) }
        indicatorBtn.setOnClickListener { _-> startPatternActivity(TYPE_WITH_INDICATOR) }
        nineBtn.setOnClickListener { _-> startPatternActivity(TYPE_9x9) }
        secureModeBtn.setOnClickListener { _-> startPatternActivity(TYPE_SECURE_MODE) }*//*

    }

    private fun startPatternActivity(type: Int) {
        val intent = Intent(this, PatternLockActivity::class.java)
        intent.putExtra(KEY_PATTERN_TYPE, type)
        startActivity(intent)
    }
}*/
