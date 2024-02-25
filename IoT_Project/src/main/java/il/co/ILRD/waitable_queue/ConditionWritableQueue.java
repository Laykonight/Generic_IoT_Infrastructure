package il.co.ILRD.waitable_queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionWritableQueue<E> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final PriorityQueue<E> queue;
    private final int capacity;

    public ConditionWritableQueue(Comparator<E> comparator, int capacity) {
        this.queue = new PriorityQueue<>(capacity, comparator);
        this.capacity = capacity;
    }

    public ConditionWritableQueue(int capacity) {
        this(null, capacity);
    }

    public boolean enqueue(E element) {
        boolean isAdded;
        try {
            this.lock.lock();
            while (this.capacity == this.size()) {
                this.notFull.await();
            }

            isAdded = this.queue.add(element);
            this.notEmpty.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);  //TODO fault tolerance
        } finally {
            this.lock.unlock();
        }

        return isAdded;
    }

    public E dequeue() {
        E temp;
        try {
            this.lock.lock();
            while (this.isEmpty()){
                this.notEmpty.await();
            }

            temp = this.queue.poll();
            this.notFull.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);  //TODO fault tolerance
        } finally {
            this.lock.unlock();
        }

        return temp;
    }

    public boolean remove(E element) {
        boolean isRemoved;
        try {
            this.lock.lock();
            isRemoved = this.queue.remove(element);
            if (isRemoved){
                this.notFull.signalAll();
            }
        } finally {
            this.lock.unlock();
        }

        return isRemoved;
    }

    public int size() {
        try {
            this.lock.lock();
            return this.queue.size();
        } finally {
            this.lock.unlock();
        }
    }

    public E peek() {
        try {
            this.lock.lock();
            return this.queue.peek();
        } finally {
            this.lock.unlock();
        }
    }

    public boolean isEmpty() {
        try {
            this.lock.lock();
            return this.queue.isEmpty();
        } finally {
            this.lock.unlock();
        }
    }
}
