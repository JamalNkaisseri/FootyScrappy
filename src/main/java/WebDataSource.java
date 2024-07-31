import org.htmlunit.*;

public class WebDataSource {


    //Configure web client instance
    public static WebClient createWebClient(){
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        return client;
    }

    public void urlConstructor(String playerName){

    }

}
