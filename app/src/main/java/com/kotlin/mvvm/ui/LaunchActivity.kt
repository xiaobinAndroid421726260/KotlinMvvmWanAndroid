package com.kotlin.mvvm.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.blankj.utilcode.util.ActivityUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityLaunchBinding

class LaunchActivity : BaseActivity() {

    private val binding by lazy { ActivityLaunchBinding.inflate(layoutInflater) }
    private lateinit var splashScreen: SplashScreen
    private val defaultExitDuration = 1500.toLong()

    override fun setWindowConfigure() {
        splashScreen = installSplashScreen()
    }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        initImmersionBar(ContextCompat.getColor(this, R.color.colorAccent))
        splashScreen.setOnExitAnimationListener { provider ->
            showSplashIconExitAnimator(provider.iconView){
                provider.remove()
            }
            // 两个动画的时长可以不一样，这里监听 splashScreen 动画结束
            showSplashExitAnimator(provider.view) {
                provider.remove()
                // 进入主界面，顺便给个 FadeOut 退场动画
                ActivityUtils.startActivity(MainActivity::class.java)
                finish()
                overridePendingTransition(0, R.anim.fade_out)
            }
        }
    }

    // Show exit animator for splash icon.
    @SuppressLint("Recycle")
    private fun showSplashIconExitAnimator(iconView: View, onExit: () -> Unit = {}) {
        val alphaOut = ObjectAnimator.ofFloat(
            iconView,
            View.ALPHA,
            1f,
            0f
        )
        // Bird scale out animator.
        val scaleOut = ObjectAnimator.ofFloat(
            iconView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0.3f, 0.3f)
            }
        )
        AnimatorSet().run {
            interpolator = AnticipateInterpolator()
            duration = defaultExitDuration
            playTogether(alphaOut, scaleOut)
            doOnEnd {
                onExit()
            }
            start()
        }
    }

    // Show exit animator for splash screen view.
    @SuppressLint("Recycle")
    private fun showSplashExitAnimator(splashScreenView: View, onExit: () -> Unit = {}) {
        // Create your custom animation set.
        val alphaOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.ALPHA,
            1f,
            0f
        )
        val scaleOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.SCALE_X,
            View.SCALE_Y,
            Path().apply {
                moveTo(1.0f, 1.0f)
                lineTo(0f, 0f)
            }
        )
        AnimatorSet().run {
            duration = defaultExitDuration
            interpolator = AnticipateInterpolator()
            playTogether(scaleOut, alphaOut)
            doOnEnd {
                onExit()
            }
            start()
        }
    }

    override fun initData() {

    }
}