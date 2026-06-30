import java.util.ArrayList;
import java.util.List;

class BSTNode {
    int data;
    BSTNode left, right;

    public BSTNode(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

public class BinarySearchTree {
    BSTNode root;

    public BinarySearchTree() {
        root = null;
    }

    // In-Order Print Elements
    public String inOrderPrintElements(BSTNode node) {
        if (node == null) return "";
        return inOrderPrintElements(node.left) +
                node.data + " " +
                inOrderPrintElements(node.right);
    }

    // Pre-Order Print Elements
    public String preOrderPrintElements(BSTNode node) {
        if (node == null) return "";
        return node.data + " " +
                preOrderPrintElements(node.left) +
                preOrderPrintElements(node.right);
    }

    // Post-Order Print Elements
    public String postOrderPrintElements(BSTNode node) {
        if (node == null) return "";
        return postOrderPrintElements(node.left) +
                postOrderPrintElements(node.right) +
                node.data + " ";
    }

    // In-Order Visit Elements
    public List<Integer> inOrderVisitElement(BSTNode node, List<Integer> result) {
        if (node != null) {
            inOrderVisitElement(node.left, result);
            result.add(node.data);
            inOrderVisitElement(node.right, result);
        }
        return result;
    }

    // Pre-Order Visit Elements
    public List<Integer> preOrderVisitElement(BSTNode node, List<Integer> result) {
        if (node != null) {
            result.add(node.data);
            preOrderVisitElement(node.left, result);
            preOrderVisitElement(node.right, result);
        }
        return result;
    }

    // Post-Order Visit Elements
    public List<Integer> postOrderVisitElement(BSTNode node, List<Integer> result) {
        if (node != null) {
            postOrderVisitElement(node.left, result);
            postOrderVisitElement(node.right, result);
            result.add(node.data);
        }
        return result;
    }

    // In-Order Visit Nodes
    public List<BSTNode> inOrderVisit(BSTNode node, List<BSTNode> result) {
        if (node != null) {
            inOrderVisit(node.left, result);
            result.add(node);
            inOrderVisit(node.right, result);
        }
        return result;
    }

    // Pre-Order Visit Nodes
    public List<BSTNode> preOrderVisit(BSTNode node, List<BSTNode> result) {
        if (node != null) {
            result.add(node);
            preOrderVisit(node.left, result);
            preOrderVisit(node.right, result);
        }
        return result;
    }

    // Post-Order Visit Nodes
    public List<BSTNode> postOrderVisit(BSTNode node, List<BSTNode> result) {
        if (node != null) {
            postOrderVisit(node.left, result);
            postOrderVisit(node.right, result);
            result.add(node);
        }
        return result;
    }

    // Método de prueba
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        bst.root = new BSTNode(10);
        bst.root.left = new BSTNode(5);
        bst.root.right = new BSTNode(15);
        bst.root.left.left = new BSTNode(2);
        bst.root.left.right = new BSTNode(7);

        System.out.println("Pre-Order Print Elements:");
        System.out.println(bst.preOrderPrintElements(bst.root));

        System.out.println("Post-Order Print Elements:");
        System.out.println(bst.postOrderPrintElements(bst.root));
    }
}
