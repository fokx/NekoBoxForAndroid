package moe.matsuri.nb4a.proxy.quicproxy;

import androidx.annotation.NonNull;

import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;

import org.jetbrains.annotations.NotNull;

import io.nekohasekai.sagernet.fmt.AbstractBean;
import io.nekohasekai.sagernet.fmt.KryoConverters;

public class QuicProxyBean extends AbstractBean {

    public static final Creator<QuicProxyBean> CREATOR = new CREATOR<QuicProxyBean>() {
        @NonNull
        @Override
        public QuicProxyBean newInstance() {
            return new QuicProxyBean();
        }

        @Override
        public QuicProxyBean[] newArray(int size) {
            return new QuicProxyBean[size];
        }
    };

    public String authKey;
    public String sni;
    public Boolean allowInsecure;
    public String certFingerprint;
    public String transport;
    public String alpn;
    public String obfsKey;

    @Override
    public void initializeDefaultValues() {
        super.initializeDefaultValues();
        if (serverPort == null || serverPort <= 0) serverPort = 4433;
        if (authKey == null) authKey = "";
        if (sni == null) sni = "";
        if (allowInsecure == null) allowInsecure = false;
        if (certFingerprint == null) certFingerprint = "";
        if (transport == null || transport.isEmpty()) transport = "quic";
        if (alpn == null || alpn.isEmpty()) alpn = "h3";
        if (obfsKey == null) obfsKey = "";
    }

    @Override
    public boolean canMapping() {
        return true;
    }

    @Override
    public void serialize(ByteBufferOutput output) {
        output.writeInt(0);
        super.serialize(output);
        output.writeString(authKey);
        output.writeString(sni);
        output.writeBoolean(allowInsecure);
        output.writeString(certFingerprint);
        output.writeString(transport);
        output.writeString(alpn);
        output.writeString(obfsKey);
    }

    @Override
    public void deserialize(ByteBufferInput input) {
        int version = input.readInt();
        super.deserialize(input);
        authKey = input.readString();
        sni = input.readString();
        allowInsecure = input.readBoolean();
        certFingerprint = input.readString();
        transport = input.readString();
        alpn = input.readString();
        obfsKey = input.readString();
    }

    @NotNull
    @Override
    public QuicProxyBean clone() {
        return KryoConverters.deserialize(new QuicProxyBean(), KryoConverters.serialize(this));
    }
}
