public class StaticHolder {
    private  StaticHolder(){ }
    public static StaticHolder getInstance(){
        return Holder.instance;
    }
    public static class Holder {
        private static final StaticHolder instance = new StaticHolder();
    }
}
