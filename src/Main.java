import Server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        while(true){
        try {
            Server.main(args);    
        } catch (Exception e) {
            //Server.main(args);
        }
    }
        
    }
}