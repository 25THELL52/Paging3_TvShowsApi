package com.example.paging3_tvshowsapi.util.networkTracker

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.time.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkStatus>
      fun  checkForWifi(): Boolean
     fun checkForData(): Boolean
     fun checkForInternet():Boolean
}

class NetworkConnectivityServiceImpl @Inject constructor(
    context: Context
) : NetworkConnectivityService {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {

            private val availableNetworks: MutableMap<String?, String> = mutableMapOf()

            override fun onAvailable(network: Network) {


                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val hasInternetCapability =
                    networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

                if (hasInternetCapability == true) {

                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.e(
                            "track internet 2 ",
                            "inside onAvailable() wifi: ${network.toString()}"
                        )

                        availableNetworks[network.toString()] = "wifi"
                    }
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.e(
                            "track internet 2 ",
                            "inside onAvailable() data: ${network.toString()}"
                        )

                        availableNetworks[network.toString()] = "cellular"
                    }


                    trySend(NetworkStatus.Connected)
                }
                Log.e("track internet 2 ", "inside onAvailable() availableNetworks: ${availableNetworks}")

            }


            override fun onUnavailable() {
                //super.onUnavailable()
                //trySend(NetworkStatus.Disconnected)
                //Log.e("track internet 2 ", "inside onUnavailable()")
                }

            override fun onLost(network: Network) {


                //Log.e("track internet 2 ", "wifi: ${checkForWifi()}")


                // make sure network that's lost is a cellular network
                val networkType: String? = availableNetworks[network.toString()]


                if (networkType.equals("cellular") && checkForWifi()) {
                    Log.e("track internet 2 ", "inside onLost() checkforwifi(): $network")
                    availableNetworks.remove(network.toString())
                }

                else if (networkType.equals("wifi") && checkForData()) {
                    Log.e("track internet 2 ", "inside onLost() checkfordata(): $network")

                    availableNetworks.remove(network.toString())

                }

                else {
                    availableNetworks.remove(network.toString())

                    trySend(NetworkStatus.Disconnected)

                    if (networkType.equals("cellular")) Log.e(
                        "track internet 2 ",
                        "inside onLost() data: $network"
                    )
                    else Log.e("track internet 2 ", "inside onLost() wifi: $network")

                }

                Log.e("track internet 2 ", "inside onLost() availableNetworks: ${availableNetworks}")
            }

        }

        /*
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

            .build()

         */
        // !!! Added this !!!
// There appears to be an issue where neither NetworkCallback.onLost or NetworkCallback.onUnavailable
// are called if the app is started without a network connection; because of this we cannot
// rely on the callback correctly initializing if offline. Work around here
// by checking activeNetwork immediately.
        connectivityManager.registerDefaultNetworkCallback(connectivityCallback)
        if (connectivityManager.activeNetwork == null) {
            Log.e("track internet 2 ", "inside onUnavailable()")

            // Run my onUnavailable code
            trySend(NetworkStatus.Disconnected)
        }
        awaitClose {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }

    }.distinctUntilChanged().flowOn(Dispatchers.IO)




     override fun checkForData(): Boolean {


        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection

        // Returns a Network object corresponding to
        // the currently active default data network.
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Cellular transport,
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    }


     override fun checkForWifi(): Boolean {


        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection

        // Returns a Network object corresponding to
        // the currently active default data network.
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // else return false
            else -> false
        }
    }



     override fun checkForInternet(): Boolean {




        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection

        // Returns a Network object corresponding to
        // the currently active default data network.
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    }



}