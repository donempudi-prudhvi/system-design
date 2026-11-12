
public class App { 
    public static void main(String[] args) throws Exception {
        Notification n = new EmailNotifier();
        n = new SMSNotifier(n);
        n = new SlackNotifier(n);
        n.addMessage("Order Shipped");
        n.send();
    }
}
