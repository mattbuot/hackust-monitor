package History;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URL_Util {
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?";

    public static void main(String[] args) throws Exception {
        System.out.println(getYTCategory("https://www.youtube.com/watch?v=Z_DS_jE2CNw"));
    }

    public static String getYTCategory(String url) throws Exception{
        if(!url.startsWith(YOUTUBE_URL)){
            return "Not a youtube video!";
        }

        URL youtube = new URL(url);
        URLConnection connection = youtube.openConnection();
        connection.addRequestProperty("Accept-language", "en-US");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        String inputLine;
        String relevant = "";
        while ((inputLine = in.readLine()) != null) {

            if(inputLine.contains("Category")) {
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
    }
}
