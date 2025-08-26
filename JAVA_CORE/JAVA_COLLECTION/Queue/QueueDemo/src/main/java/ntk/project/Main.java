package ntk.project;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(null);
        System.out.println("Test Queue 1");
        System.out.println(queue.poll());
        System.out.println(queue);
        System.out.println("Test Queue 2");
        Queue<Integer> queue2 = new ArrayDeque<>();
        queue2.offer(1);
        queue2.offer(2);
        queue2.offer(3);
        System.out.println(queue2);
        System.out.println("Test Queue 3");
        Queue<Integer> queue3 = new PriorityQueue<>(
                (new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        if(o1==o2) return 0;
                        return (o1>o2)?-1:1;
                    }
                })
        );
        queue3.offer(2);
        queue3.offer(1);
        queue3.offer(3);
        System.out.println(queue3);
        Deque<Integer> deque = new LinkedList<>();
        deque.addFirst(1);
        deque.addLast(2);
        deque.addLast(3);
        System.out.println(deque);
    }
}