package study.singleton;

// 싱글톤 객체는 무조건 무상태성으로 설계해야해!!!!
public class StatefulService {
//    private int price; // 상태를 유지하는 필드
    public int order(String name, int price){
        System.out.println("name = " + name + "price = " + price);
//        this.price = price; // 여기가 문제
        return price;
    }
//
//    public int getPrice(){
//        return price;
//    }
}
