public class SMSNotifier extends NotifierDecorator {
   
    SMSNotifier(Notification wrapped){
        super(wrapped);
    }

    public void send() {
        super.send();
        for(String message : messages){
            System.out.println("SMS Sent : "+ message);
        }
    }
    
}
