public class LazyInitialization {
    private static LazyInitialization instance;
    private  LazyInitialization(){}
    public static LazyInitialization getInstance(){
        if(instance == null){
            try { 
                Thread.sleep(1); 
            } 
            catch (InterruptedException e) {

            } //for demo purpose, actual instance initialization may take time in real time
            
            instance = new LazyInitialization();
        }
        return instance;
    }
    
}
