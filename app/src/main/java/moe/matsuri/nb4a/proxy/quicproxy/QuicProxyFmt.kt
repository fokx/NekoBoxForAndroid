package moe.matsuri.nb4a.proxy.quicproxy

import io.nekohasekai.sagernet.ktx.linkBuilder
import io.nekohasekai.sagernet.ktx.toLink
import io.nekohasekai.sagernet.ktx.urlSafe
import io.nekohasekai.sagernet.ktx.wrapIPV6Host
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

fun QuicProxyBean.buildQuicProxyConfig(port: Int): String {
    val sb = StringBuilder()
    sb.append("socks5_listen = \"127.0.0.1:$port\"\n\n")
    sb.append("[[servers]]\n")
    sb.append("name = \"default\"\n")
    sb.append("server_addr = \"${finalAddress.wrapIPV6Host()}\"\n")
    sb.append("server_port = ${finalPort ?: 4433}\n")
    sb.append("auth_key = \"${authKey.replace("\\", "\\\\").replace("\"", "\\\"")}\"\n")
    val trans = if (!transport.isNullOrBlank()) transport else "quic"
    sb.append("transport = \"$trans\"\n")
    if (allowInsecure == true) {
        sb.append("insecure = true\n")
    }
    var effectiveSni = sni
    if (effectiveSni.isNullOrBlank() && finalAddress == "127.0.0.1" && !serverAddress.isNullOrBlank()) {
        effectiveSni = serverAddress
    }
    if (!effectiveSni.isNullOrBlank()) {
        sb.append("server_name = \"${effectiveSni.replace("\"", "\\\"")}\"\n")
    }
    if (!certFingerprint.isNullOrBlank()) {
        sb.append("cert_fingerprint = \"${certFingerprint.trim()}\"\n")
    }
    if (!alpn.isNullOrBlank() && alpn != "h3") {
        sb.append("\n[servers.tls_fingerprint]\n")
        sb.append("alpn = [\"${alpn.replace("\"", "\\\"")}\"]\n")
    }
    if (!obfsKey.isNullOrBlank()) {
        sb.append("\n[servers.obfuscation]\n")
        sb.append("enabled = true\n")
        sb.append("key = \"${obfsKey.replace("\"", "\\\"")}\"\n")
    }
    return sb.toString()
}

fun QuicProxyBean.toUri(): String {
    val builder = linkBuilder()
        .host(serverAddress)
        .port(serverPort)
    if (!authKey.isNullOrBlank()) {
        builder.username(authKey)
    }
    if (!name.isNullOrBlank()) {
        builder.encodedFragment(name.urlSafe())
    }
    if (allowInsecure == true) {
        builder.addQueryParameter("insecure", "1")
    }
    if (!sni.isNullOrBlank()) {
        builder.addQueryParameter("sni", sni)
    }
    if (!certFingerprint.isNullOrBlank()) {
        builder.addQueryParameter("cert_fingerprint", certFingerprint)
    }
    if (!transport.isNullOrBlank() && transport != "quic") {
        builder.addQueryParameter("transport", transport)
    }
    if (!alpn.isNullOrBlank() && alpn != "h3") {
        builder.addQueryParameter("alpn", alpn)
    }
    if (!obfsKey.isNullOrBlank()) {
        builder.addQueryParameter("obfs", obfsKey)
    }
    return builder.toLink("quicproxy")
}

fun parseQuicproxy(url: String): QuicProxyBean {
    val link = url.replace("quicproxy://", "https://").toHttpUrlOrNull() ?: error(
        "invalid quicproxy link $url"
    )
    return QuicProxyBean().apply {
        serverAddress = link.host
        serverPort = link.port
        name = link.fragment
        authKey = link.username
        if (authKey.isEmpty() && link.password.isNotEmpty()) {
            authKey = link.password
        }
        link.queryParameter("sni")?.let { sni = it }
        link.queryParameter("insecure")?.let {
            allowInsecure = it == "1" || it.equals("true", ignoreCase = true)
        }
        link.queryParameter("allow_insecure")?.let {
            allowInsecure = it == "1" || it.equals("true", ignoreCase = true)
        }
        (link.queryParameter("cert_fingerprint") ?: link.queryParameter("fp") ?: link.queryParameter("fingerprint"))?.let {
            certFingerprint = it
        }
        link.queryParameter("transport")?.let { transport = it }
        link.queryParameter("alpn")?.let { alpn = it }
        (link.queryParameter("obfs") ?: link.queryParameter("obfs_key"))?.let { obfsKey = it }
        initializeDefaultValues()
    }
}
