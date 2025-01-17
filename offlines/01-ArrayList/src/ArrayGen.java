import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ArrayGen<T> {
    public static final String DEBUG = "Array->";
    public static final int BASE_SIZE = 2;
    public static final int MAX_SIZE = 1 << 31 - 1; // 2^31 - 1;
    private Object[] data;
    private int length;

    /**
     * create an {@link #Array()} of a predefined size
     */
    ArrayGen() {
        data = new String[BASE_SIZE];
        length = 0;
    }

    /**
     * create an array of length n
     * 
     * @param n is the size of the {@link #Array()}
     */
    ArrayGen(int n) {
        int size = BASE_SIZE;
        while (BASE_SIZE < n)
            size = BASE_SIZE << 1; //double the size

        data = new String[size];
        length = n;
    }

    /**
     * Initialize the array with the given list of elements
     * 
     * @param ar is the array of string
     */
    ArrayGen(Object[] ar) {
        int size = BASE_SIZE;
        while (size < ar.length) {
            size = size << 1; // double the size
        }

        this.data = new Object[size];
        for (int i = 0; i < ar.length; i++) {
            data[i] = ar[i];
        }
        this.length = ar.length;
    }

    /**
     * resize the array when needed
     * 
     * @param size is the new size of array(data)
     */
    private void resizeAndCopyArray(int size) {
        if (size > MAX_SIZE) {
            throw new RuntimeException("Array already reached max size");
        }

        // System.out.println(DEBUG+"resizeAndCopyArray(): new size: "+size);
        Object[] temp = new Object[size];
        for (int i = 0; i < data.length; i++) {
            temp[i] = data[i]; // copy all the data
        }
        this.data = temp;
    }

    private void vallidateIndex(int idx) {
        if (idx < 0 || idx >= length)
            throw new IndexOutOfBoundsException("index out of bound. index 0 to " + (length - 1) + " is allowed");
    }

    /**
     * 
     * @return the array itself
     */
    public ArrayGen<T> getArray() {
        return this;
    }


    /**
     * return i th element of the array
     * 
     * @param i index of the element we want to access
     * @return the value at index i;
     * @throws IndexOutOfBoundsException if the index is not valid
     * 
     */
    public T getAnElement(int i) {
        vallidateIndex(i);
        return (T)data[i];
    }

    /**
     * add an element at the end of the array and if the array is full, double the
     * size of the array and insert it
     * 
     * @param s the string to be added
     * @throws RuntimeException the max array size is reached
     */
    public void add(Object s) {
        if (length == MAX_SIZE) // array reached max size
            throw new RuntimeException("Can't append. Array has already reached max size");

        if (length == data.length)// array is full
            resizeAndCopyArray(length * 2); // allocate double memory

        data[length++] = s; // add element at the end and increase the length
    }

    /**
     * add the element on the i-th position of the array
     * 
     * @param idx is the index where string will be added
     * @param str is the value that will be added
     * @throws IndexOutOfBoundsException if the index is not valid
     */
    public void add(int idx, Object str) {
        vallidateIndex(idx);

        if (idx == length) {
            add(str); // append the element at the end
        }

        // if(length == data.length || length + 1 = data.length)
        if (length + 1 >= data.length) // check if inserting an array overflow the array size
            resizeAndCopyArray(data.length * 2); // double the size

        // move all the element 1 index right starting from idx
        for (int i = length; i >= idx; i--) {
            data[i + 1] = data[i]; // move array element one index right
        }
        data[idx] = str;
        length++;
    }

    /**
     * remove all the elements that match with the given element from the array that
     * is equal to param e this method calls {@link #removeAt(int)} method to remove
     * each matched element
     * 
     * @param e is string e want remove from the list
     */
    public void remove(Object e) {
        int idx = indexOf(0, e); // find the 1st occurence
        if (idx == -1)
            return;

        while (idx != -1) {
            removeAt(idx);
            idx = indexOf(idx + 1, e); // find the next occurence if exist
        }

    }

    private void removeAt(int idx) {
        vallidateIndex(idx);

        for (int i = idx + 1; i < length; i++) {
            data[i - 1] = data[i]; // move item one index left
        }
        length--;
    }

    /**
     * return the indexes of all the occurences where the given element are found
     * 
     * @param e
     * @return an array of of index
     */
    public int[] findIndex(Object e) {
        int cnt = 0;
        // count the occurence
        for (int i = 0; i < length; i++) {
            if (data[i].equals(e))
                cnt++;
        }

        int[] idxs = {};
        if (cnt > 0) {
            // allocate memory
            idxs = new int[cnt];
        }

        // store all the index in the array
        for (int i = 0, j = 0; i < length; i++) {
            if (data[i].equals(e))
                idxs[j++] = i;
        }
        return idxs;
    }

    /**
     * search from the start if string is found return 1st occurence index else
     * return -1
     * 
     * 
     * @param start is the start index
     * @param e     is string to be searched
     * @return the index is found else -1
     * 
     */
    private int indexOf(int start,Object e) {
        if (start < 0 || start > length)
            return -1;

        for (int i = start; i < length; i++) {
            if (data[i].equals(e))
                return i;
        }
        return -1;
    }

    /**
     * return all elements within the given range, from start to end of the array
     * 
     * @param start start index of the subarray(inclusive)
     * @param end   end index of the subarray(inclusive)
     * @return an Array
     * @throws IndexOutOfBoundsException is start or end index is out of bound
     */
    public ArrayGen<T> subArray(int start, int end) {
        vallidateIndex(start);
        vallidateIndex(end);

        Object[] temp = {};
        if (start >= length)
            return new ArrayGen<T>(temp);

        int size = end - start + 1;
        if (size > 0) {
            temp = new Object[size];
            for (int i = 0; i < size; i++) {
                temp[i] = data[start + i];
            }
        }
        return new ArrayGen<T>(temp);
    }

    /**
     * populate the array with the merged list of two sorted arrays A1 and A2 (note
     * that, A1 and A2 are sorted
     * 
     * @param ar1 is the 1st sorted array
     * @param ar2 is the 2nd sorted array
     */
    public void merge(Object[] ar1, Object[] ar2, Comparator<T> comparator) {

        int size = BASE_SIZE;
        length = ar1.length + ar2.length;
        while (size < length) {
            size = size << 1;
            // System.out.println(DEBUG + "merge(): size: "+ size);
        }
        // System.out.println(DEBUG + "merge(): new array length "+ size);
        this.data = new Object[size];

        for (int i = 0, j = 0, k = 0; k < length; k++) {
            if (i == ar1.length && j == ar2.length)
                return;

            if (i == ar1.length) // ar1 is inserted already
                data[k] = ar2[j++];
            else if (j == ar2.length) // ar2 is alredy inserted
                data[k] = ar1[i++];

            // else compare elements
            else if (comparator.compare((T)ar1[i], (T)(ar2[j])) < 0)
                data[k] = ar1[i++];
            else
                data[k] = ar2[j++];

            System.out.print(data[k] + " ");
        }
        System.out.println();
    }

    public int length() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public static void main(String[] args) {
        try {
            Integer[] temp = { 1, 3, 4 };
            Integer[] temp1 = { 3, 10 , 4, 15 };
            Arrays.sort(temp1);
            ArrayGen<Integer> ar = new ArrayGen<>();
            System.out.println("length: " + ar.length());
            System.out.println("isEmpty? : " + ar.isEmpty() + "\n");

            ar.merge(temp, temp1, new Comparator<Integer>(){
               @Override
               public int compare(Integer o1, Integer o2) {
                    return o1 - o2;
               }
            });

            /* System.out.println("GET array data: ");
            for (Integer integer : ar.getArrayData()) {
                System.out.print(integer + " ");
            }
            System.out.println("\n"); */

            System.out.println("Initial elements: ");
            for (int i = 0; i < ar.length(); i++) {
                System.out.print(ar.getAnElement(i) + " ");
            }
            System.out.println("\n" + "length: " + ar.length() + "\n");

            ar.add("app");
            System.out.println("Elements after appending an element: ");
            for (int i = 0; i < ar.length(); i++) {
                System.out.print(ar.getAnElement(i) + " ");
            }
            System.out.println("\n" + "length: " + ar.length() + "\n");

            ar.add(2, "ins");
            System.out.println("Elements after inserting an element at idx 2: ");
            for (int i = 0; i < ar.length(); i++) {
                System.out.print(ar.getAnElement(i) + " ");
            }
            System.out.println("\n" + "length: " + ar.length() + "\n");

            System.out.println("Subarray from index 1 to 5: ");
            ArrayGen<Integer> subAr = ar.subArray(1, 5);
            for (int i = 0; i < subAr.length(); i++) {
                System.out.print(subAr.getAnElement(i) + " ");
            }
            System.out.println("\n\n");

            System.out.println("Index's of '2': ");
            for (int i : ar.findIndex("2")) {
                System.out.print("i: " + i + "  ");
            }
            System.out.println("\n\n");

            ar.remove("2");
            System.out.println("Elements after removing all 2s: ");
            for (int i = 0; i < ar.length(); i++) {
                System.out.print(ar.getAnElement(i) + " ");
            }
            System.out.print("\n" + "length: " + ar.length() + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}