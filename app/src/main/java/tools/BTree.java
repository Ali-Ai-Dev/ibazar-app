package tools;

/**
 * Created by Omid on 5/19/2018.
 */

public class BTree {
    private int id;
    private BTree left_child;
    private BTree right_child;

    public BTree(int id) {
        this.id = id;
    }

    public boolean add(BTree node) {
        if (node.id == id)
            return true;
        if (node.id > id) {
            if (right_child == null) {
                right_child = node;
                return false;
            } else
                return right_child.add(node);
        } else {
            if (left_child == null) {
                left_child = node;
                return false;
            } else
                return left_child.add(node);
        }
    }

//    public boolean contains(int id) {
//        if (this.id == id)
//            return true;
//        if (this.id < id)
//            return right_child.contains(id);
//        else return left_child.contains(id);
//    }
}
