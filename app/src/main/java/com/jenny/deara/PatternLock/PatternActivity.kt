package com.jenny.deara.PatternLock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.jenny.deara.PatternLock.PatternSecureModeActivity.Companion.KEY_PATTERN_TYPE
import com.jenny.deara.R
import com.jenny.deara.componet.PatternViewStageState
import com.jenny.deara.componet.PatternViewState
import com.jenny.deara.databinding.ActivityPatternBinding
import com.jenny.deara.viewmodel.PatternLockViewModel
import kotlinx.android.synthetic.main.activity_pattern.*
import java.util.regex.Pattern

//패턴 처음 설정할 때
class PatternActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPatternBinding

    private val viewModel by viewModels<PatternLockViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)

        binding = ActivityPatternBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        clear_TextButton.setOnClickListener {
            viewModel.updateViewState(PatternViewState.Initial)
        }

        stage_Button . setOnClickListener {
            when (pattern_LockView.stageState) {
                PatternViewStageState.FIRST -> {
                viewModel.updateViewState(PatternViewState.Initial)

                pattern_LockView.stageState = PatternViewStageState.SECOND
                stage_Button.text = getString(R.string.stage_button_confirm)

                    //팝업 띄우기
                    binding.stageButton.setOnClickListener {
                        val dialog = CustomDialog()
                        dialog.show(supportFragmentManager, "CustomDialog")
                    }

                }
                PatternViewStageState.SECOND -> {
                    AlertDialog.Builder(this).apply {
                    setMessage(R.string.alert_dialog_confirm_message)
                    setPositiveButton(R.string.alert_dialog_positive_button) { _, _ -> }
                    }.show()

                }
            }
        }

        pattern_LockView . setOnChangeStateListener { state ->
            viewModel.updateViewState(state)
        }

            lifecycleScope . launchWhenCreated {
                viewModel.viewState.collect { patternViewState ->
                    when (patternViewState) {
                        is PatternViewState.Initial -> {
                            pattern_LockView.reset()
                            stage_Button.isEnabled = false
                            clear_TextButton.isVisible = false
                            tvMessage.run {
                                text =
                                    if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
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
                                text =
                                    if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
                                        getString(R.string.success_message_first_stage)
                                    } else {
                                        getString(R.string.nothing)
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
                                text =
                                    if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
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
                        is PatternViewState.Secure -> {
                            tvMessage.run {
                                text =
                                    if (pattern_LockView.stageState == PatternViewStageState.FIRST) {
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