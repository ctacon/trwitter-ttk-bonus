package trwitter.ttk.bonus;

import java.io.IOException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 *
 * @author ctacon
 */
public class NetworkManager {

    public static HttpClient httpClient;

    public static boolean tryCode(String code) throws IOException, Exception {
//        code = ;
        System.out.println("Отправляю код " + code);
        PostMethod method = new PostMethod("/ajax/action2014/activate_code.php");
        method.addParameter("code", code.substring(0, 3) + " " + code.substring(3, 6) + " " + code.substring(6, 10));
        int i = 1;
        for (String part : code.split("-")) {
            method.addParameter("c" + i, part);
            i++;
        }

        method.addRequestHeader(new Header("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.106 Safari/535.2"));
//	method.addRequestHeader(new Header("Accept","application/json, text/javascript, */*; q=0.01" ));
//	method.addRequestHeader(new Header("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.3" ));
        method.addRequestHeader(new Header("", ""));
        int kod = httpClient.executeMethod(method);
        System.out.println("cod = " + kod);
        JsonMap<String, String> map = new JsonMap<String, String>(method.getResponseBodyAsString());
        System.out.println("map = " + map);
        if (map != null) {
            if (map.get("valid") != null && map.get("valid").equals("true")) {
                System.out.println("Нашел правильный код!");
                System.out.println("Код ошибки = " + map.get("error"));
                return true;
            } else {
                System.out.println("Ответ не содержит правды");
                return false;
            }
        } else {
            System.out.println("Получил ошибку при разборе ответа");
            return false;
        }
    }

    public static void login(String user, String password) {
        try {
            MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
            HttpConnectionManagerParams params = new HttpConnectionManagerParams();
            params.setDefaultMaxConnectionsPerHost(3);
            params.setMaxTotalConnections(3);
            params.setSoTimeout(60 * 1000);
            params.setConnectionTimeout(60 * 1000);
            connectionManager.setParams(params);


            HostConfiguration configuration = new HostConfiguration();
            configuration.setHost("bonus.myttk.ru", Integer.parseInt("443"), new Protocol("https", new VeryEasySSLProtocolSocketFactory(), 443));
            httpClient = new HttpClient(connectionManager);
            httpClient.setHostConfiguration(configuration);

            PostMethod method = new PostMethod("/spend/action2014/index.php?login=yes");
            method.addParameter(new NameValuePair("AUTH_FORM", "Y"));
            method.addParameter(new NameValuePair("TYPE", "AUTH"));
            method.addParameter(new NameValuePair("USER_LOGIN", user));
            method.addParameter(new NameValuePair("USER_PASSWORD", password));
            method.addParameter(new NameValuePair("USER_REMEMBER", "Y"));
            int kod = httpClient.executeMethod(method);
            if (kod == 200) {
                System.out.println("Авторизация прошла успешно!");
            } else {
                System.out.println("Ошибка авторизации!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
