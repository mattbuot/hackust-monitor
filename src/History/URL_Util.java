package History;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URL_Util {

    public static void main(String[] args) throws Exception {

        URL youtube = new URL("https://www.youtube.com/watch?v=y3niFzo5VLI");
        URLConnection connection = youtube.openConnection();
        connection.addRequestProperty("Accept-language", "en-US");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        String inputLine;
        String relevant = "";
        //System.out.println("URL openned!");
        int i = 0;
        while ((inputLine = in.readLine()) != null) {
            i++;

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
        System.out.println(relevant);
        in.close();
    }

    public static String getYTCategory(String url) {
        if(!url.startsWith("https://www.youtube.com/watch?")){
            return "Not a youtube video!";
        }
        return null;


    }
}
