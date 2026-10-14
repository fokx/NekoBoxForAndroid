package moe.matsuri.nb4a.proxy.quicproxy

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import io.nekohasekai.sagernet.Key
import io.nekohasekai.sagernet.R
import io.nekohasekai.sagernet.database.preference.EditTextPreferenceModifiers
import io.nekohasekai.sagernet.ktx.applyDefaultValues
import io.nekohasekai.sagernet.ui.profile.ProfileSettingsActivity
import moe.matsuri.nb4a.proxy.PreferenceBinding
import moe.matsuri.nb4a.proxy.PreferenceBindingManager
import moe.matsuri.nb4a.proxy.Type

class QuicProxySettingsActivity : ProfileSettingsActivity<QuicProxyBean>() {
    override fun createEntity() = QuicProxyBean().applyDefaultValues()

    private val pbm = PreferenceBindingManager()
    private val name = pbm.add(PreferenceBinding(Type.Text, "name"))
    private val serverAddress = pbm.add(PreferenceBinding(Type.Text, "serverAddress"))
    private val serverPort = pbm.add(PreferenceBinding(Type.TextToInt, "serverPort"))
    private val authKey = pbm.add(PreferenceBinding(Type.Text, "authKey"))
    private val sni = pbm.add(PreferenceBinding(Type.Text, "sni"))
    private val allowInsecure = pbm.add(PreferenceBinding(Type.Bool, "allowInsecure"))
    private val certFingerprint = pbm.add(PreferenceBinding(Type.Text, "certFingerprint"))
    private val transport = pbm.add(PreferenceBinding(Type.Text, "transport"))
    private val alpn = pbm.add(PreferenceBinding(Type.Text, "alpn"))
    private val obfsKey = pbm.add(PreferenceBinding(Type.Text, "obfsKey"))

    override fun QuicProxyBean.init() {
        pbm.writeToCacheAll(this)
    }

    override fun QuicProxyBean.serialize() {
        pbm.fromCacheAll(this)
    }

    override fun PreferenceFragmentCompat.createPreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        addPreferencesFromResource(R.xml.quicproxy_preferences)

        findPreference<EditTextPreference>(Key.SERVER_PORT)!!.apply {
            setOnBindEditTextListener(EditTextPreferenceModifiers.Port)
        }
        findPreference<EditTextPreference>("authKey")!!.apply {
            summaryProvider = PasswordSummaryProvider
        }
    }
}
