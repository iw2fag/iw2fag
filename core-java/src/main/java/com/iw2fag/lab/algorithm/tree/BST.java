package com.iw2fag.lab.algorithm.tree;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/19/2017
 * Time: 1:14 PM
 */

/**
 * a simple implement of BST. Recursion Version, do not use generic type.
 */
public class BST {

    private Node root;

    public Node get(int value) {
        return get(this.root, value);
    }

    private Node get(Node root, int value) {
        if (root.value == value) {
            return root;
        } else if (value < root.value) {
            return get(root.left, value);
        } else {
            return get(root.right, value);
        }
    }

    public Node min() {
        return min(this.root);
    }

    private Node min(Node root) {
        if (root.left == null) {
            return root;
        }
        return min(root.left);
    }

    public Node max() {
        return max(this.root);
    }

    private Node max(Node root) {
        if (root.right == null) {
            return root;
        }
        return max(root.right);
    }

    public void printNode() {
        this.printNode(this.root);
    }

    private void printNode(Node root) {

        if (root.left != null) {
            printNode(root.left);
        }

        System.out.println(root);

        if (root.right != null) {
            printNode(root.right);
        }
    }

    public Node add(int value) {
        return add(this.root, value);
    }

    private Node add(Node root, int value) {
        if (this.root == null) {
            this.root = new Node(value);
            return this.root;
        }
        if (root == null) {
            return new Node(value);
        }

        if (value < root.value) {
            root.left = add(root.left, value);
        }

        if (value > root.value) {
            root.right = add(root.right, value);
        }

        return root;

    }

    public Node floor(int base) {
        return floor(this.root, base);
    }

    private Node floor(Node x, int base) {

        if (x == null) {
            return null;
        }

        if (base == x.value) {
            return x;
        }
        if (base < x.value) {
            return floor(x.left, base);
        }

        Node t = floor(x.right, base);

        if (t != null) {
            return t;
        }
        return x;
    }

    public Node deleteMin() {
        return deleteMin(this.root);
    }

    private Node deleteMin(Node root) {
        if (root == null) {
            return null;
        }

        if (root.left == null) {
            return root.right;
        }
        root.left = deleteMin(root.left);
        return root;
    }

    public Node delete(int value) {
        return delete(this.root, value);
    }

    private Node delete(Node root, int value) {
        if (root == null) {
            return null;
        }

        if (value < root.value) {
            root.left = delete(root.left, value);
        } else if (value > root.value) {
            root.right = delete(root.right, value);
        } else {

            if (root.right == null) {
                return root.left;
            }

            if (root.left == null) {
                return root.right;
            }

            Node t = root;
            root = min(t.right);
            root.right = deleteMin(t.right);
            root.left = t.left;
        }
        return root;
    }

    public boolean isValidBST(Node root) {
        return isValidBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isValidBST(Node root, int min, int max) {

        if (root == null) {
            return true;
        }

        if (root.value < min || root.value > max) {
            return false;
        }

        return isValidBST(root.left, min, root.value) && isValidBST(root, root.value, max);
    }

    public int getDepth(Node root) {

        if (root == null) {
            return 0;
        }

        int m = getDepth(root.left) + 1;
        int n = getDepth(root.right) + 1;

        return m > n ? m : n;
    }


    public Node getRoot() {
        return this.root;
    }


    private class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node:" + this.value;
        }
    }

}
