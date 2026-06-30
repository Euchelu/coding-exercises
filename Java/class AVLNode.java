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
