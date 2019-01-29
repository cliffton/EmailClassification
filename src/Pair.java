// ###############################################################################
//
// This is a generic function that that is used to pair two diffrent type of objects
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ###############################################################################

/**
 * This class is used to pair two objects
 */
public class Pair<T, E> {

    T email;
    E similarity;

    /**
     * This constructor is used for creating an object of this class
     * @param email This the meail we are trying to pair with its similarith
     * @param similarity similarity value
     */
    public Pair(T email, E similarity) {
        this.email = email;
        this.similarity = similarity;
    }

    /**
     * This method returns the similarity value of the email.
     * @return Similarity value.
     */
    public E getValue() {
        return similarity;
    }

    /**
     * This method returns the value of the key i.e. email in this case
     * @return Email
     */
    public T getKey() {
        return email;
    }


}
