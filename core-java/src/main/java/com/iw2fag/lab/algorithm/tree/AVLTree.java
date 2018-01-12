package com.iw2fag.lab.algorithm.tree;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 1/12/2018
 * Time: 4:11 PM
 */

/**
 * AVL tree, try to use none-recursive to implement
 * Implement add(Include fix), delete(don't include fix, blocked by technical shortage:()
 *
 * @param <E>
 */
public class AVLTree<E extends Comparable<E>> {
    public static void main(String[] args) {
        AVLTree<Integer> s = new AVLTree<>();
        s.add(10);
        s.add(5);
        s.add(15);
        s.add(7);
        s.add(4);
        s.add(3);
        s.add(8);
        s.add(6);
        s.printNode();
    }

    private Node<E> root;

    /**
     * delete one node which value equals to the parameter
     *
     * @param value
     * @return
     */
    public E delete(E value) {

        if (this.root == null) {
            return null;
        }

        Node<E> c = this.root;
        int cmp;
        while (c != null) {
            cmp = value.compareTo(c.value);

            if (cmp < 0) {
                c = c.left;
            } else if (cmp > 0) {
                c = c.right;
            } else {

                Node<E> parent = parentOf(c);
                //  System.out.println("Parent:" + parent);

                //case1:right == null. left whatever
                if (c.right == null) {
                    if (c.value.compareTo(parent.value) > 0) {
                        parent.right = c.left;
                    } else {
                        parent.left = c.left;
                    }
                    return c.value;
                }

                //case2:right != null, left == null
                if (c.left == null) {
                    if (c.value.compareTo(parent.value) > 0) {
                        parent.right = c.right;
                    } else {
                        parent.left = c.right;
                    }
                    return c.value;
                }

                //case3: right and left all != null
                Node<E> min = min(c.right);
                deleteMin(c.right);
                Node<E> t = c;
                c = min;
                c.left = t.left;
                c.right = t.right;
                if (t.value.compareTo(parent.value) < 0) {
                    parent.left = c;
                } else {
                    parent.right = c;
                }
                return t.value;
            }
        }

        //Todo fix after deletion
        return null;

    }


    public Node<E> parentOf(Node<E> node) {
        return node == null ? null : node.parent;
    }

    /**
     * delete the minimal child node of the parameter
     *
     * @param root
     * @return
     */
    public void deleteMin(Node<E> root) {

        if (root == null) {
            return;
        }

        /**
         * Case1: this tree contains only one node.
         */
        if (root.left == null && root.right == null) {
            Node<E> parent = parentOf(root);
            if (parent == null) {
                //root node
                this.root = null;
            } else {
                //has parent node
                if (parent.value.compareTo(root.value) > 0) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
            }
            return;
        }

        /**
         * Case2: this tree has child nodes.
         */
        Node<E> parent = root;
        while (root.left != null) {
            parent = root;
            root = root.left;
        }
        parent.left = root.right;
    }

    /**
     * get the minimal child node of the Node
     *
     * @param root
     * @return
     */
    public Node<E> min(Node<E> root) {

        if (root == null) {
            return null;
        }

        while (root.left != null) {
            root = root.left;
        }
        return root;
    }


    /**
     * add one new node to this BST.
     *
     * @param value
     * @return the previous value associated with Node
     */
    public E add(E value) {
        if (this.root == null) {
            this.root = new Node<>(value, null, 1);
            return null;
        }

        Node<E> c = this.root;
        Node<E> parent;
        int cmp;
        do {
            parent = c;
            cmp = value.compareTo(c.value);
            if (cmp < 0) {
                c = c.left;
            } else if (cmp > 0) {
                c = c.right;
            } else {
                E previousValue = c.value;
                c.value = value;
                return previousValue;
            }
        } while (c != null);
        //

        c = new Node<>(value, parent, 1);
        if (cmp > 0) {
            parent.right = c;
        } else {
            parent.left = c;
        }

        updateParentHeight(c);
        //fix after insertion
        return fixAfterInsertion(c);
    }

    private E fixAfterInsertion(Node<E> c) {
        E v = c.value;
        while (c != null) {

            Node<E> newRootNode;
            if (height(c.left) - height(c.right) == 2) {
                //the node was added at left, possible fix: LL LR
                if (v.compareTo(c.left.value) < 0) {
                    newRootNode = LLRotation(c);
                } else {
                    newRootNode = LRRotation(c);
                }

                fixNodeAfterRotation(newRootNode);

            } else if (height(c.left) - height(c.right) == -2) {
                if (v.compareTo(c.right.value) < 0) {
                    newRootNode = RRRotation(c);
                } else {
                    newRootNode = RLRotation(c);
                }
                fixNodeAfterRotation(newRootNode);
            }
            c = c.parent;
        }

        return null;
    }


    private void fixNodeAfterRotation(Node<E> node) {
        // System.out.println("new Node" + node);
        if (node.parent != null) {
            if (node.value.compareTo(node.parent.value) < 0) {
                node.parent.left = node;
            } else {
                node.parent.right = node;
            }
        }

        updateParentHeight(node);
    }


    private void updateParentHeight(Node<E> node) {
        while (node.parent != null) {
            Node<E> parent = node.parent;
            parent.height = Math.max(height(parent.left), height(parent.right)) + 1;
            node = parent;
        }
    }

    private int height(Node<E> node) {
        if (node != null) {
            return node.height;
        }
        return 0;
    }

    /**
     * left left rotation.
     *
     * @param root: Not balanced tree root node
     */
    private Node<E> LLRotation(Node<E> root) {
        Node<E> leftChild = root.left;

        root.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = root;
        }

        leftChild.parent = root.parent;
        if (root.parent == null) {
            this.root = leftChild;
        }
        root.parent = leftChild;
        leftChild.right = root;


        root.height = Math.max(height(root.right), height(root.left)) + 1;
        leftChild.height = Math.max(height(leftChild.left), height(leftChild.right)) + 1;

        return leftChild;
    }


    /**
     * right right rotation.
     *
     * @param root: the tree'root node which is not balanced
     */
    private Node<E> RRRotation(Node<E> root) {
        Node<E> rightChild = root.right;

        root.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = root;
        }

        rightChild.parent = root.parent;
        if (root.parent == null) {
            this.root = rightChild;
        }
        root.parent = rightChild;
        rightChild.left = root;

        root.height = Math.max(height(root.right), height(root.left)) + 1;
        rightChild.height = Math.max(height(rightChild.left), height(rightChild.right)) + 1;
        return rightChild;
    }

    private Node<E> LRRotation(Node<E> root) {
        Node<E> newRootNode = RRRotation(root.left);
        root.left = newRootNode;
        newRootNode.left.parent = newRootNode;

        return LLRotation(root);
    }

    private Node<E> RLRotation(Node<E> root) {
        Node<E> newRootNode = LLRotation(root.right);
        root.right = newRootNode;
        newRootNode.right.parent = newRootNode;
        return RRRotation(root);
    }


    public void printNode() {
        this.printNode(this.root);
    }

    private void printNode(Node<E> root) {

        if (root.left != null) {
            printNode(root.left);
        }

        System.out.println(root);

        if (root.right != null) {
            printNode(root.right);
        }
    }


    private static class Node<E> {
        E value;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        int height;

        Node(E value, Node<E> parent, int height) {
            this.value = value;
            this.parent = parent;
            this.height = height;
        }

        @Override
        public String toString() {
            return "Node(value:" + this.value + ", height:" + this.height + ",parent:" + (this.parent == null ? null : this.parent.value) + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!(obj instanceof Node)) {
                return false;
            }

            if (this == obj) {
                return true;
            }
            Node<E> node = (Node<E>) obj;
            return this.value.equals(node.value);
        }
    }


}
