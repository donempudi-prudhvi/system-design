public class SynchronizedMethod {
    private static SynchronizedMethod instance;
    private SynchronizedMethod(){}
    public  static synchronized SynchronizedMethod getInstance(){
        if(instance == null){
            instance = new SynchronizedMethod();
        }
        return instance;
    }
}
