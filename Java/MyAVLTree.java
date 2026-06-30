public class MyAVLTree<E extends Comparable<E>> extends MyBinarySearchTree<E> {

    // =========================================================
    // 1. GESTIÓN DE ALTURAS Y FACTOR DE EQUILIBRIO
    // =========================================================

    private int height(BinaryNode<E> node) {
        return (node == null) ? 0 : node.height;
    }

    private void updateHeight(BinaryNode<E> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(BinaryNode<E> node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // =========================================================
    // 2. ROTACIONES (MECÁNICA)
    // =========================================================

    // Rotación Simple a la Derecha
    private BinaryNode<E> rotateRight(BinaryNode<E> y) {
        BinaryNode<E> x = y.left;
        BinaryNode<E> T2 = x.right;

        // Rotación
        x.right = y;
        y.left = T2;

        // Actualizar alturas
        updateHeight(y);
        updateHeight(x);

        return x; // x es la nueva raíz
    }

    // Rotación Simple a la Izquierda
    private BinaryNode<E> rotateLeft(BinaryNode<E> x) {
        BinaryNode<E> y = x.right;
        BinaryNode<E> T2 = y.left;

        // Rotación
        y.left = x;
        x.right = T2;

        // Actualizar alturas
        updateHeight(x);
        updateHeight(y);

        return y; // y es la nueva raíz
    }

    // =========================================================
    // 3. BALANCEO (CEREBRO)
    // =========================================================

    private BinaryNode<E> balance(BinaryNode<E> node) {
        updateHeight(node);
        int balance = getBalance(node);

        // Caso Izquierda Pesada (Left-Left o Left-Right)
        if (balance > 1) {
            // Si el hijo izquierdo pesa a la derecha -> Rotación Doble
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        // Caso Derecha Pesada (Right-Right o Right-Left)
        if (balance < -1) {
            // Si el hijo derecho pesa a la izquierda -> Rotación Doble
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    // =========================================================
    // 4. SOBRESCRITURA DE MÉTODOS ABB (INTEGRACIÓN)
    // =========================================================

    @Override
    public void addElement(E element) {
        // Asumimos que 'root' es accesible (protected) o usamos un setter si es private
        super.root = addRecursiveAVL(super.root, element);
    }

    private BinaryNode<E> addRecursiveAVL(BinaryNode<E> node, E element) {
        // 1. Inserción normal de BST
        if (node == null) {
            return new BinaryNode<>(element); // Asegurar que el constructor inicie height en 1
        }

        int compare = element.compareTo(node.element);
        if (compare < 0) {
            node.left = addRecursiveAVL(node.left, element);
        } else if (compare > 0) {
            node.right = addRecursiveAVL(node.right, element);
        } else {
            return node; // No duplicados
        }

        // 2. Paso Extra: Balancear el nodo antes de volver
        return balance(node);
    }

    @Override
    public E removeElement(E element) {
        if (!find(element))
            return null;
        super.root = removeRecursiveAVL(super.root, element);
        return element;
    }

    private BinaryNode<E> removeRecursiveAVL(BinaryNode<E> node, E element) {
        // 1. Borrado normal de BST
        if (node == null)
            return null;

        int compare = element.compareTo(node.element);

        if (compare < 0) {
            node.left = removeRecursiveAVL(node.left, element);
        } else if (compare > 0) {
            node.right = removeRecursiveAVL(node.right, element);
        } else {
            // Nodo encontrado
            if (node.left == null)
                return node.right;
            if (node.right == null)
                return node.left;

            // Caso dos hijos: buscar sucesor (mínimo del subárbol derecho)
            BinaryNode<E> temp = findMinNode(node.right);
            node.element = temp.element;
            node.right = removeRecursiveAVL(node.right, temp.element);
        }

        // 2. Paso Extra: Balancear tras el borrado
        return balance(node);
    }

    // Método auxiliar necesario si no es visible desde la clase padre
    private BinaryNode<E> findMinNode(BinaryNode<E> node) {
        while (node.left != null)
            node = node.left;
        return node;
    }
}