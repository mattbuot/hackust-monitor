package History;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URL_Util {
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?";

    public static String getYTCategory(String url) {
        String relevant = "";
        try {
            if (!url.startsWith(YOUTUBE_URL)) {
                return null;
            }

            URL youtube = new URL(url);
            URLConnection connection = youtube.openConnection();
            connection.addRequestProperty("Accept-language", "en-US");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {

                if (inputLine.contains("Category")) {
                    in.readLine();
                    in.readLine();
                    relevant = in.readLine();
                    break;
                }
            }
            //System.out.println(relevant);
            relevant = relevant.substring(0, relevant.lastIndexOf("</a>"));
            //System.out.println(relevant);
            relevant = relevant.substring(relevant.lastIndexOf(">") + 1);
            //System.out.println(relevant);
            in.close();

            return relevant;
        } catch (Exception e) {
            System.out.println("ERROR -> relevant: " + relevant);
            System.err.println(e.getMessage());
        }
        return null;
    }
}
