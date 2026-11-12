import java.util.*;
public class EmailNotifier implements Notification {
    private final List<String> messages = new ArrayList<>();
    
    public void addMessage(String message) {
        messages.add(message);
    }

    public void send() {
        for(String message : messages){
            System.out.println("Sending email : "+ message);
        }
    }   

}

