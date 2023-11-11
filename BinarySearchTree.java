import java.util.Scanner;
import java.util.Random;

public class BinarySearchTree<E extends Comparable<E>> {
	// ---------------- nested Node class ----------------
	private static class Node<E> {
		private E data; // reference to the data stored at this node
		private Node<E> left; // reference to the left child
		private Node<E> right; // reference to the right child
		private Node<E> parent; // reference to the parent

		public Node(E e) {
			data = e;
			left = right = parent = null;
		}

	}
	
	private long numDataCompares = 0;
	private int leftRight;

	private Node<E> root = null; // root of the tree (or null if empty)

	public BinarySearchTree() {
	}

	// insert a new value into a binary search tree
	// if the value already exists, the tree is not changed
	public E insert(E data) {
		Node<E> p = new Node<E>(data);
		if (root == null) // if the tree is empty, put this value in the root
			root = p;
		else {
			Node<E> q = find(data);
			if (leftRight < 0) { // it should be in the left subtree
				p.parent = q;
				q.left = p;
			} else if (leftRight > 0) { // it should be in the right subtree
				p.parent = q;
				q.right = p;
			} else
				; // duplicate data -- do nothing
		}
		return data;
	}

	// find a value in a binary search tree
	// if it doesn't exist, return the node
	// that should be its parent
	public Node<E> find(E data) {
		if (root == null)
			return null;
		Node<E> p = root;
		while (p != null) {
			leftRight = data.compareTo(p.data);
			numDataCompares++;
			if (leftRight < 0) { // it should be in the left subtree
				if (p.left == null)
					return p;
				else
					p = p.left;
			}
			else if (leftRight > 0) { // it should be in the right subtree
				if (p.right == null)
					return p;
				else
					p = p.right;
			}
			else {
				return p; // found it
			}
		}
		return p; // it should be in a subtree of this node
	}

	// perform an inorder traversal printing the values
	public void inorder(Node<E> p) {
		if (p == null)
			return;
		inorder(p.left);
		System.out.print(p.data + " ");
		inorder(p.right);
	}
	
	// perform an preorder traversal printing the values
	public void preorder(Node<E> p) {
		if (p == null)
			return;
		System.out.print(p.data + " ");
		preorder(p.left);
		preorder(p.right);
	}
	
	// perform an postorder traversal printing the values
	public void postorder(Node<E> p) {
		if (p == null)
			return;
		postorder(p.left);
		postorder(p.right);
		System.out.print(p.data + " ");
	}
	
	// find the preorder successor of a given value
	public E preorderNext(E data) {
		Node<E> p = find(data);
		if (p == null) return null;
		if (p.left != null) return p.left.data;
		if (p.right != null) return p.right.data;
		// if it is a leaf, use the parent link to move up the tree
		Node<E> q = p.parent;
		while (q != null && (q.right == p || q.right == null)) {
			p = q;
			q = p.parent;
		}
		if (q == null) return null;
		return q.right.data;
	}

	// find the inorder successor of a given value
	public E inorderNext(E data) {
		Node<E> p = find(data);
		if (p == null) return null;
		if (p.right != null) {		// there is a right subtree
			numDataCompares++;
			p = p.right;		// move to that subtree
			while (p.left != null)	// move down left subtree
				p = p.left;
			return p.data;		// found it
		}
		Node<E> q = p.parent;
		// otherwise, search for an ancestor that 
		//    is the left child of its parent
		while (q != null &&  p != q.left) {
			p = q;
			q = p.parent;
		}
		if (q == null) return null;
		return q.data;
	}

	// find the postorder successor of a given value
	public E postorderNext(E data) {
		Node<E> p = find(data);
		if (p == null) return null;
		Node<E> q = p.parent;
		if (q == null) return null;		// root has no postorder successor
		if (p == q.right)			// if this node is the right child
			return q.data;		//     then the parent is the successor
		// otherwise it is the left child
		if (q.right == null)		// if there is no sibling
			return q.data;		// return the parent
		// otherwise move to the sibling and look for a leaf
		q = q.right;
		while (q.left != null || q.right != null)
			if (q.left != null) q = q.left;
			else q = q.right;
		return q.data;
	}


	
	public static void main(String[] args) {
		Scanner scr = new Scanner(System.in);
		Random rnd = new Random();
		int size;
		while((size = scr.nextInt()) > 0) {
			long startTime = System.nanoTime();
// create a tree containing uniformly distributed pseudorandom ints
			BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();
			for (int i=0; i<size; i++) {
				int x = rnd.nextInt();
				bst.insert(x);
			}
			long elapsed = System.nanoTime() - startTime;
			System.out.println("compares = " + bst.numDataCompares + "   time= " + elapsed/1e9);

			long startTime2 = System.nanoTime();
			BinarySearchTree<Integer> bst2 = new BinarySearchTree<Integer>();
			for(int i=0; i<size; i++){
				int y = i+1;
				bst2.insert(y);
			}
			long elapsed2 = System.nanoTime() - startTime2;
			System.out.println("compares = " + bst2.numDataCompares + "   time= " + elapsed2/1e9);
		}
		scr.close();
	}
}