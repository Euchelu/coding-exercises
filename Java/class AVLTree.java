import java.util.ArrayList;
import java.util.List;

class AVLNode {
    int data, height;
    AVLNode left, right;

    public AVLNode(int data) {
        this.data = data;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}

public class AVLTree {
    AVLNode root;

    public AVLTree() {
        root = null;
    }

    // Método para insertar elementos balanceados en el árbol AVL
    public void insert(int data) {
        root = insertRec(root, data);
    }

    private AVLNode insertRec(AVLNode node, int data) {
        if (node == null)
            return new AVLNode(data);

        if (data < node.data)
            node.left = insertRec(node.left, data);
        else if (data > node.data)
            node.right = insertRec(node.right, data);
        else
            return node; // No se permiten duplicados

        // Actualizar la altura
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private AVLNode balance(AVLNode node) {
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1 && getBalance(node.left) >= 0)
            return rotateRight(node);
        if (balanceFactor > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balanceFactor < -1 && getBalance(node.right) <= 0)
            return rotateLeft(node);
        if (balanceFactor < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        y.left = x.right;
        x.right = y;

        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        x.right = y.left;
        y.left = x;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }

    // === Métodos de recorrido inOrder ===

    // In-Order: Devuelve una lista de elementos
    public List<Integer> inOrderVisitElements(AVLNode node, List<Integer> result) {
        if (node != null) {
            inOrderVisitElements(node.left, result);
            result.add(node.data);
            inOrderVisitElements(node.right, result);
        }
        return result;
    }

    // In-Order: Imprime los elementos
    public void inOrderPrint(AVLNode node) {
        if (node != null) {
            inOrderPrint(node.left);
            System.out.print(node.data + " ");
            inOrderPrint(node.right);
        }
    }

    // In-Order: Devuelve una lista de nodos
    public List<AVLNode> inOrderVisit(AVLNode node, List<AVLNode> result) {
        if (node != null) {
            inOrderVisit(node.left, result);
            result.add(node);
            inOrderVisit(node.right, result);
        }
        return result;
    }

    // === Métodos de recorrido preOrder ===

    // Pre-Order: Devuelve una lista de elementos
    public List<Integer> preOrderVisitElements(AVLNode node, List<Integer> result) {
        if (node != null) {
            result.add(node.data);
            preOrderVisitElements(node.left, result);
            preOrderVisitElements(node.right, result);
        }
        return result;
    }

    // Pre-Order: Imprime los elementos
    public void preOrderPrint(AVLNode node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preOrderPrint(node.left);
            preOrderPrint(node.right);
        }
    }

    // Pre-Order: Devuelve una lista de nodos
    public List<AVLNode> preOrderVisit(AVLNode node, List<AVLNode> result) {
        if (node != null) {
            result.add(node);
            preOrderVisit(node.left, result);
            preOrderVisit(node.right, result);
        }
        return result;
    }

    // === Métodos de recorrido postOrder ===

    // Post-Order: Devuelve una lista de elementos
    public List<Integer> postOrderVisitElements(AVLNode node, List<Integer> result) {
        if (node != null) {
            postOrderVisitElements(node.left, result);
            postOrderVisitElements(node.right, result);
            result.add(node.data);
        }
        return result;
    }

    // Post-Order: Imprime los elementos
    public void postOrderPrint(AVLNode node) {
        if (node != null) {
            postOrderPrint(node.left);
            postOrderPrint(node.right);
            System.out.print(node.data + " ");
        }
    }

    // Post-Order: Devuelve una lista de nodos
    public List<AVLNode> postOrderVisit(AVLNode node, List<AVLNode> result) {
        if (node != null) {
            postOrderVisit(node.left, result);
            postOrderVisit(node.right, result);
            result.add(node);
        }
        return result;
    }

    // Método principal para probar
    public static void main(String[] args) {
        AVLTree avl = new AVLTree();
        avl.insert(10);
        avl.insert(20);
        avl.insert(30);
        avl.insert(5);
        avl.insert(15);

        System.out.println("In-Order Print:");
        avl.inOrderPrint(avl.root);

        System.out.println("\nPre-Order Print:");
        avl.preOrderPrint(avl.root);

        System.out.println("\nPost-Order Print:");
        avl.postOrderPrint(avl.root);

        List<Integer> inOrderElements = avl.inOrderVisitElements(avl.root, new ArrayList<>());
        System.out.println("\nIn-Order Elements: " + inOrderElements);

        List<Integer> preOrderElements = avl.preOrderVisitElements(avl.root, new ArrayList<>());
        System.out.println("Pre-Order Elements: " + preOrderElements);

        List<Integer> postOrderElements = avl.postOrderVisitElements(avl.root, new ArrayList<>());
        System.out.println("Post-Order Elements: " + postOrderElements);
    }
}
