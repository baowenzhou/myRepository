package Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TestArrayList {
    public static void main(String[] args)  {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(2);
        Iterator<Integer> iterator = list.iterator();
        while(iterator.hasNext()){
            Integer integer = iterator.next();
            if(integer==2)
//                list.remove(integer);//会报ConcurrentModificationException异常
            	iterator.remove();   //注意这个地方，这样正确
        }
    }
}