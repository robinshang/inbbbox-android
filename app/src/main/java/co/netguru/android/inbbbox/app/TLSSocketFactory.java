package co.netguru.android.inbbbox.app;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openssl.PEMParser;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import javax.net.ssl.SSLSocket;

public class TLSSocketFactory extends SSLSocketFactory {

    private SSLSocketFactory internalSSLSocketFactory;

    public TLSSocketFactory(Context c) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);
        internalSSLSocketFactory = getSSLSocketFactory(c, null);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return internalSSLSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return internalSSLSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket() throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket());
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        if(socket != null && (socket instanceof SSLSocket)) {
            ((SSLSocket)socket).setEnabledProtocols(new String[] {"TLSv1.1", "TLSv1.2"});
        }
        return socket;
    }

    public static SSLSocketFactory getSSLSocketFactory(Context ActContext,
                                                       final String caCrtFile) {

        try {
            /**
             * Add BouncyCastle as a Security Provider
             */
            Security.addProvider(new BouncyCastleProvider());

            JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter().setProvider("BC");

            /**
             * Load Certificate Authority (CA) certificate
             */

            PEMParser reader = null;
            X509CertificateHolder caCertHolder = null;
            X509Certificate caCert = null;
            if(caCrtFile != null){
                reader = new PEMParser(new InputStreamReader(ActContext.getAssets().open(caCrtFile)));
                caCertHolder = (X509CertificateHolder) reader.readObject();
                reader.close();

                caCert = certificateConverter.getCertificate(caCertHolder);
            }

            /**
             * CA certificate is used to authenticate server
             */
            TrustManagerFactory trustManagerFactory = null;
            if(caCert != null){

                KeyStore caKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                caKeyStore.load(null, null);
                caKeyStore.setCertificateEntry("ca-certificate", caCert);

                trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(caKeyStore);
            }else{
                Log.d("TAG","getSSLSocketFactory : NULL caCert");
            }

            /**
             * Create SSL socket factory
             */
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            if(trustManagerFactory != null){
                Log.d("TAG","context.init with root ca");
                context.init(null, trustManagerFactory.getTrustManagers(), null);
            } else {
                Log.d("TAG","context.init without root ca");
                context.init(null, null, null);
            }


            /**
             * Return the newly created socket factory object
             */
            return context.getSocketFactory();    // 2016-07-20

        } catch (Exception e) {

            Log.d("TAG","getSSLSocketFactory: exception #####################");
            e.printStackTrace();
        }

        return null;
    }
}