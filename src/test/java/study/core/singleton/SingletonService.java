package study.core.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance(){
        return instance;
    }
    // 생성을 막는거지
    private SingletonService(){}
}
