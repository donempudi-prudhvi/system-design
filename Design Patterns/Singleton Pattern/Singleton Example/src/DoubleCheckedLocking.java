public class DoubleCheckedLocking {
    private  static DoubleCheckedLocking instance;
    private DoubleCheckedLocking(){}
    public static DoubleCheckedLocking getInstance(){
        if(instance == null){
            synchronized(DoubleCheckedLocking.class){
                instance = new DoubleCheckedLocking();
            }
        }
        return  instance;
    }
}
