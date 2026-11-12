import java.util.*;

public abstract class NotifierDecorator implements Notification {
    protected final Notification wrapped;
    protected final List<String> messages = new ArrayList<>();

    public NotifierDecorator(Notification wrapped) {
        System.out.println("Called NotifierDecorator Constructor from " + getClass().getSimpleName());
        this.wrapped = wrapped;
    }

    public void addMessage(String message) {
        System.out.println("Called NotifierDecorator addMessage Method from " + getClass().getSimpleName());
        wrapped.addMessage(message);
        messages.add(message);
    }

    public void send() {
        wrapped.send();
    }
    
}
