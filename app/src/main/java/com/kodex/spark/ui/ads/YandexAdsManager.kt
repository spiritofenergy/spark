package com.kodex.spark.ui.ads

import android.content.Context
import com.kodex.spark.MainActivity
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import javax.inject.Singleton

@Singleton
class YandexAdsManager(private val  context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null

    init {
        interstitialAdLoader = InterstitialAdLoader(context).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    // The ad was loaded successfully. Now you can show loaded ad.
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                }
            })
        }
        loadInterstitialAd()
    }
        private fun loadInterstitialAd() {
            val adRequestConfiguration =
                AdRequestConfiguration.Builder("demo-interstitial-yandex").build()
            interstitialAdLoader?.loadAd(adRequestConfiguration)
        }

         fun showAd(context: Context, onAdDismissed: () -> Unit) {
            interstitialAd?.apply {
                setAdEventListener(object : InterstitialAdEventListener {
                    override fun onAdShown() {
                        // Called when ad is shown.
                    }

                    override fun onAdFailedToShow(adError: AdError) {
                        // Called when an InterstitialAd failed to show.
                        // Clean resources after Ad dismissed
                        interstitialAd?.setAdEventListener(null)
                        interstitialAd = null

                        // Now you can preload the next interstitial ad.
                        loadInterstitialAd()
                        onAdDismissed()
                    }

                    override fun onAdDismissed() {
                        // Called when ad is dismissed.
                        // Clean resources after Ad dismissed
                        interstitialAd?.setAdEventListener(null)
                        interstitialAd = null

                        // Now you can preload the next interstitial ad.
                        loadInterstitialAd()
                        onAdDismissed()
                    }

                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                    }

                    override fun onAdImpression(impressionData: ImpressionData?) {
                        // Called when an impression is recorded for an ad.
                    }
                })
                show(context as MainActivity)
            }
        }
    }
