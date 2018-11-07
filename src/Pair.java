public class Pair<T, E> {

    T email;
    E d;

    public Pair(T email, E d) {
        this.email = email;
        this.d = d;
    }


    public E getValue() {
        return d;
    }

    public T getKey() {
        return email;
    }


}
