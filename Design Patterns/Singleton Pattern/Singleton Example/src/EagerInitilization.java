public class EagerInitilization {
    private static final  EagerInitilization instance = new EagerInitilization();
    private EagerInitilization(){}
    public static EagerInitilization getInstance(){
        return instance;
    }
}
