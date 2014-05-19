
package trwitter.ttk.bonus;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;


public class VeryEasySSLProtocolSocketFactory implements
	SecureProtocolSocketFactory {

    private SSLContext sslcontext;
    private String keystoreUrl;
    private String keystorePassword;
    private String keystoreType;

    public VeryEasySSLProtocolSocketFactory() {
    }

    public VeryEasySSLProtocolSocketFactory(String keystoreUrl, String keystorePassword, String keystoreType) {
	this.keystoreUrl = keystoreUrl;
	this.keystorePassword = keystorePassword;
	this.keystoreType = keystoreType;
    }

    private static KeyManager[] createKeyManagers(final KeyStore keystore, final String password)
	    throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
	if (keystore == null) {
	    throw new IllegalArgumentException("Keystore may not be null");
	}
	KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
		KeyManagerFactory.getDefaultAlgorithm());
	kmfactory.init(keystore, password != null ? password.toCharArray() : null);
	return kmfactory.getKeyManagers();
    }

    private SSLContext createSSLContext() throws IOException {
	try {

	    KeyManager[] keymanagers = null;
	    if (this.keystoreUrl != null) {
		KeyStore keystore = KeyStore.getInstance(keystoreType);
		keystore.load(new FileInputStream(keystoreUrl), keystorePassword.toCharArray());
		keymanagers = createKeyManagers(keystore, this.keystorePassword);
	    }

	    TrustManager[] trustmanagers = null;
	    trustmanagers = new TrustManager[1];
	    trustmanagers[0] = new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		    return null;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}
	    };
	    SSLContext sslc = SSLContext.getInstance("SSL");
	    sslc.init(keymanagers, trustmanagers, new SecureRandom());
	    return sslc;
	} catch (NoSuchAlgorithmException e) {
	    throw new IOException(e);
	} catch (GeneralSecurityException e) {
	    throw new IOException(e);
	}
    }

    public SSLContext getSSLContext() throws IOException {
	if (this.sslcontext == null) {
	    this.sslcontext = createSSLContext();
	}
	return this.sslcontext;
    }

    public Socket createSocket(  
	    final String host,
	    final int port,
	    final InetAddress localAddress,
	    final int localPort,
	    final HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
	if (params == null) {
	    throw new IllegalArgumentException("Parameters may not be null");
	}
	int timeout = params.getConnectionTimeout();
	Socket socket = null;
	if (timeout == 0) {
	    socket = createSocket(host, port, localAddress, localPort);
	} else {
	    socket = ControllerThreadSocketFactory.createSocket(
		    this, host, port, localAddress, localPort, timeout);
	}
	return socket;
    }

    public Socket createSocket(
	    String host,
	    int port,
	    InetAddress clientHost,
	    int clientPort)
	    throws IOException, UnknownHostException {
	return getSSLContext().getSocketFactory().createSocket(
		host,
		port,
		clientHost,
		clientPort);
    }

    public Socket createSocket(String host, int port)
	    throws IOException, UnknownHostException {
	return getSSLContext().getSocketFactory().createSocket(
		host,
		port);
    }

    public Socket createSocket(
	    Socket socket,
	    String host,
	    int port,
	    boolean autoClose)
	    throws IOException, UnknownHostException {
	return getSSLContext().getSocketFactory().createSocket(
		socket,
		host,
		port,
		autoClose);
    }
}
