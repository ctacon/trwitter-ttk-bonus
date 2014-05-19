package trwitter.ttk.bonus;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import twitter4j.*;
import twitter4j.auth.AccessToken;

/**
 *
 * @author ctacon
 */
public class TrwitterTtkBonus {
    
    public static String user = "";//
    public static String password = "";//"";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException {
        try {
            
            TwitterStreamFactory factory = new TwitterStreamFactory();
            AccessToken accessToken = new AccessToken("255512579-Idnk1Z0PvL4IQFtfwmOcmEiCLccwshkTeqx9NZcW", "h5JxNbrCf4eNigyMOPckOsA72V1bRo7zxAnx0pMxFzw");
            TwitterStream twitter = factory.getInstance();
            twitter.setOAuthConsumer("U21xEowAFiRmfRiN7dtEXw", "KHW0VX0HvTvxbcV6XlnVKAi1i5RL4dQAf0E70olGEg");
            twitter.setOAuthAccessToken(accessToken);
            StatusListener listener;
            listener = new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                    Matcher matcher = Pattern.compile("(\\d[а-яА-Яa-zA-z\\d,\\*,\\+,\\-]{8,40})").matcher(status.getText());
                    String text = "";
                    if (matcher.find()) {
                        text = matcher.group(1);
                        text = text.trim().replace(" ", "").replace("\n", "").replace("\t", "").replace(";", "");
                        System.out.println("Получил сообщение: " + text);
                        List<String> codes = processCode(text);
                        System.out.println("Получил варианты кодов " + codes);
                        NetworkManager.login(user, password);
                        for (String code : codes) {
                            try {
                                System.out.println("Обрабатываю код " + code);
                                boolean result = NetworkManager.tryCode(code);
                                System.out.println("Получил результат: " + result);
                                if (result == true) {
                                    JOptionPane.showMessageDialog(null, "Получил успех для кода " + code + "  пользователь " + user);
                                    System.exit(0);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        
                    } else {
                        System.out.println("Число не найдено в твите " + status.getText());
                    }
                }
                
                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                }
                
                @Override
                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                }
                
                @Override
                public void onScrubGeo(long userId, long upToStatusId) {
                }
                
                @Override
                public void onStallWarning(StallWarning warning) {
                }
                
                @Override
                public void onException(Exception ex) {
                }
            };
           
            twitter.addListener(listener);
            twitter.filter(new FilterQuery(0,
                    new long[]{124149764},
                    new String[]{"124149764"}));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to search tweets: " + ex.getMessage());
            System.exit(-1);
        }
    }
    
    public static List<String> processCode(String text) {
        try {
            text = text.trim().replace(" ", "")
                    .replace("ноль", "0")
                    .replace("нль", "0")
                    .replace("нол", "0")
                    .replace("один", "1")
                    .replace("одн", "1")
                    .replace("адин", "1")
                    .replace("два", "2")
                    .replace("дв", "2")
                    .replace("три", "3")
                    .replace("тр", "3")
                    .replace("четыре", "4")
                    .replace("четые", "4")
                    .replace("чтые", "4")
                    .replace("четы", "4")
                    .replace("пять", "5")
                    .replace("пть", "5")
                    .replace("пят", "5")
                    .replace("шесть", "6")
                    .replace("шсть", "6")
                    .replace("шест", "6")
                    .replace("семь", "7")
                    .replace("сем", "7")
                    .replace("восемь", "8")
                    .replace("восем", "8")
                    .replace("восмь", "8")
                    .replace("девять", "9")
                    .replace("девть", "9")
                    ;
            //пробую перебрать варианты
            List<String> variants = new LinkedList<String>();
            variants.add(text);
            //если нужно подбирать число
            if (text.contains("*")) {
                System.out.println("Нужно заменить *");
                for (int i = 0; i < 10; i++) {
                    String s = text.replace("*", i + "");
                    if (!variants.contains(s) ) {
                        variants.add(s);
                    }
                }
            } else {
//                //если есть запятые
//                String[] parts = text.split(",");
//                System.out.println("Получил частей " + parts.length);
//
//                for (int i = 0; i < parts.length; i++) {
//                    for (int k = 0; k < parts.length; k++) {
//                        String part1 = parts[i];
//                        String s = Calculation.getInstance().calculate(part1.trim());
//                        for (int j = k; j < parts.length; j++) {
//                            String part2 = parts[j];
//                            if (s.length() >= 10) {
//                                break;
//                            }
//                            if (i != j) {
//                                s += Calculation.getInstance().calculate(part2.trim());
//                            }
//                        }
//                        if (!variants.contains(s) && s.length() == 10) {
//                            variants.add(s);
//                        }
//                    }
//                }
            }
            List<String> reversed = new LinkedList<String>();
            for (String var : variants) {
                reversed.add(new StringBuffer(var).reverse().toString());
            }
            variants.addAll(reversed);
            return variants;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
