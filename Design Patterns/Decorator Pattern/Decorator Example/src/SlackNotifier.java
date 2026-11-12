public class SlackNotifier extends NotifierDecorator{

    SlackNotifier(Notification wrapped){
        super(wrapped);
    }

    public void send() {
        super.send();
        for(String message : messages){
            System.out.println("Slacked : "+ message);
        }
    }
    
}
