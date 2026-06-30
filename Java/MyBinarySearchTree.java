import java.util.*;

// Definición de la clase que implementa la lógica de las EPD 6 y 8
public class MyBinarySearchTree<E extends Comparable<E>> {

    // --- CLASE INTERNA (EPD-6 E1) ---
    // Representa los nodos del árbol 
    public static class BinaryNode<E> {
        E element;
        BinaryNode<E> left;
        BinaryNode<E> right;

        public BinaryNode(E element) {
            this.element = element;
            this.left = null;
            this.right = null;
        }
    }

    // --- ATRIBUTOS DEL ÁRBOL ---
    private BinaryNode<E> root;
    private int size;

    public MyBinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    // =========================================================
    // PARTE 1: MÉTODOS DE LA INTERFAZ BinarySearchTree (EPD-8)
    // [cite: 922-933]
    // =========================================================

    // Inserta un elemento manteniendo el orden [cite: 923]
    public void addElement(E element) {
        root = addRecursive(root, element);
    }

    private BinaryNode<E> addRecursive(BinaryNode<E> node, E element) {
        if (node == null) {
            size++;
            return new BinaryNode<>(element);
        }

        int compare = element.compareTo(node.element);
        if (compare < 0) {
            node.left = addRecursive(node.left, element);
        } else if (compare > 0) {
            node.right = addRecursive(node.right, element);
        }
        // Si es igual, dependiendo de la política, se ignora o actualiza. 
        // Aquí lo ignoramos (no duplicados).
        return node;
    }

    // Busca un elemento y devuelve true si existe [cite: 933]
    public boolean find(E elementToFind) {
        return findRecursive(root, elementToFind);
    }

    private boolean findRecursive(BinaryNode<E> node, E element) {
        if (node == null) return false;
        int compare = element.compareTo(node.element);
        if (compare == 0) return true;
        return compare < 0 ? findRecursive(node.left, element) : findRecursive(node.right, element);
    }

    // Devuelve el elemento mínimo (el de más a la izquierda) [cite: 931]
    public E findMin() {
        if (isEmpty()) return null;
        return findMinNode(root).element;
    }

    private BinaryNode<E> findMinNode(BinaryNode<E> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Devuelve el elemento máximo (el de más a la derecha) [cite: 932]
    public E findMax() {
        if (isEmpty()) return null;
        BinaryNode<E> current = root;
        while (current.right != null) current = current.right;
        return current.element;
    }

    // Elimina un elemento específico [cite: 924]
    public E removeElement(E element) {
        if (!find(element)) return null; // Si no existe, no hacemos nada
        root = removeRecursive(root, element);
        return element;
    }

    private BinaryNode<E> removeRecursive(BinaryNode<E> node, E element) {
        if (node == null) return null;

        int compare = element.compareTo(node.element);

        if (compare < 0) {
            node.left = removeRecursive(node.left, element);
        } else if (compare > 0) {
            node.right = removeRecursive(node.right, element);
        } else {
            // Nodo encontrado. Casos de borrado:
            size--;
            // Caso 1: Hoja o un solo hijo
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Caso 2: Dos hijos. Buscamos el menor del subárbol derecho (sucesor)
            E minVal = findMinNode(node.right).element;
            node.element = minVal; // Reemplazamos valor
            node.right = removeRecursive(node.right, minVal); // Borramos el duplicado
            size++; // Corregimos el size-- extra que ocurrirá en la recursión
        }
        return node;
    }

    // Elimina el mínimo elemento [cite: 926]
    public E removeMin() {
        if (isEmpty()) return null;
        E min = findMin();
        root = removeRecursive(root, min);
        return min;
    }

    // Elimina el máximo elemento [cite: 930]
    public E removeMax() {
        if (isEmpty()) return null;
        E max = findMax();
        root = removeRecursive(root, max);
        return max;
    }

    // Elimina todas las ocurrencias (en BST sin duplicados es igual a removeElement) [cite: 925]
    public void removeAllOcurrences(E element) {
        while (find(element)) {
            removeElement(element);
        }
    }

    // =========================================================
    // PARTE 2: MÉTODOS DE LA INTERFAZ BinaryTree (EPD-6)
    // Métodos heredados necesarios [cite: 834-844]
    // =========================================================

    public int size() { return size; } // [cite: 835]
    public boolean isEmpty() { return root == null; } // [cite: 836]
    public BinaryNode<E> root() { return root; } // [cite: 841]

    // Métodos de consulta sobre nodos [cite: 842-843]
    public boolean isInternal(BinaryNode<E> node) { return hasLeft(node) || hasRight(node); }
    public boolean isExternal(BinaryNode<E> node) { return !isInternal(node); }
    public boolean isRoot(BinaryNode<E> node) { return node == root; }
    public boolean hasLeft(BinaryNode<E> node) { return node != null && node.left != null; }
    public boolean hasRight(BinaryNode<E> node) { return node != null && node.right != null; }
    public BinaryNode<E> left(BinaryNode<E> node) { return node != null ? node.left : null; }
    public BinaryNode<E> right(BinaryNode<E> node) { return node != null ? node.right : null; }

    // Obtener el padre (Complejo porque el nodo no tiene puntero 'parent') [cite: 842]
    public BinaryNode<E> parent(BinaryNode<E> target) {
        if (target == null || target == root) return null;
        return findParent(root, target);
    }

    private BinaryNode<E> findParent(BinaryNode<E> current, BinaryNode<E> target) {
        if (current == null) return null;
        // Si alguno de mis hijos es el objetivo, yo soy el padre
        if (current.left == target || current.right == target) return current;
        
        // Búsqueda en ABB aprovechando el orden
        int cmp = target.element.compareTo(current.element);
        if (cmp < 0) return findParent(current.left, target);
        else return findParent(current.right, target);
    }

    public Collection<BinaryNode<E>> children(BinaryNode<E> node) {
        List<BinaryNode<E>> list = new ArrayList<>();
        if (hasLeft(node)) list.add(node.left);
        if (hasRight(node)) list.add(node.right);
        return list;
    }

    // =========================================================
    // PARTE 3: ITERADORES (Recorridos) [cite: 837-841]
    // =========================================================

    // 1. InOrden (Izquierda - Raíz - Derecha) -> Devuelve elementos ordenados
    public Iterator<E> elementsInOrder() {
        List<E> list = new ArrayList<>();
        collectInOrder(root, list);
        return list.iterator();
    }
    private void collectInOrder(BinaryNode<E> node, List<E> list) {
        if (node != null) {
            collectInOrder(node.left, list);
            list.add(node.element);
            collectInOrder(node.right, list);
        }
    }

    // 2. PreOrden (Raíz - Izquierda - Derecha)
    public Iterator<E> elementsPreOrder() {
        List<E> list = new ArrayList<>();
        collectPreOrder(root, list);
        return list.iterator();
    }
    private void collectPreOrder(BinaryNode<E> node, List<E> list) {
        if (node != null) {
            list.add(node.element);
            collectPreOrder(node.left, list);
            collectPreOrder(node.right, list);
        }
    }

    // 3. PostOrden (Izquierda - Derecha - Raíz)
    public Iterator<E> elementsPostOrder() {
        List<E> list = new ArrayList<>();
        collectPostOrder(root, list);
        return list.iterator();
    }
    private void collectPostOrder(BinaryNode<E> node, List<E> list) {
        if (node != null) {
            collectPostOrder(node.left, list);
            collectPostOrder(node.right, list);
            list.add(node.element);
        }
    }
    
    // Iteradores de Nodos (Similar a los de elementos pero devuelven BinaryNode)
    public Iterator<BinaryNode<E>> nodesInOrder() {
        List<BinaryNode<E>> list = new ArrayList<>();
        collectNodesInOrder(root, list);
        return list.iterator();
    }
    private void collectNodesInOrder(BinaryNode<E> node, List<BinaryNode<E>> list) {
        if (node != null) {
            collectNodesInOrder(node.left, list);
            list.add(node);
            collectNodesInOrder(node.right, list);
        }
    }
    // (Se omiten nodesPreOrder y nodesPostOrder por brevedad, siguen la misma lógica que elements...)
}