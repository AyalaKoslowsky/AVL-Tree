
/**
 *
 *
 * AVLTree
 *
 * An implementation of an AVL Tree with distinct integer keys and info.
 *
 */

public class AVLTree {
	
	// fields of AVLTree
	static IAVLNode virtualNode = new VirtualNode();
	private IAVLNode root;
	private int size;

	// empty tree-constructor
	public AVLTree() {
		this.root = null;
		this.size = 0;
	}
	
	// non empty tree-constructor
	public AVLTree(IAVLNode root) {
		this.root = root;
		this.size = (this.root.getLeft().getSize() + this.root.getRight().getSize() + 1);
		this.root.setParent(null);
	}
	
	
	
	
	/**
	 * public boolean empty()
	 *
	 * Returns true if and only if the tree is empty.
	 *
	 */
	// complexity: O(1)
	public boolean empty() {
		return this.root == null;
	}
	
	
	// recursive helper for search method, in in-order walk search
	//returns null if k is not in the tree, otherwise returns the node with key value k
	// complexity: O(logn)
	private IAVLNode search(IAVLNode node, int k) {
		if (node.isRealNode() == false) {
			return null;
		}
		if (node.getKey() == k) {
			return node;
		}

		if (node.getKey() > k) {
			return search(node.getLeft(), k);
		} else {
			return search(node.getRight(), k);
		}
	}

	/**
	 * public String search(int k)
	 *
	 * Returns the info of an item with key k if it exists in the tree. otherwise,
	 * returns null.
	 */
	// complexity: O(logn)
	public String search(int k) {
		if (this.root == null) {
			return null;
		}

		IAVLNode node = search(this.root, k);
		if (node == null) {
			return null;
		}
		return node.getValue();
	}
	
	
	
	// increases node`s rank by 1
	// complexity: O(1)
	private void promote(IAVLNode node) {
		node.setHeight(node.getHeight() + 1);
	}

	// decreases node`s rank by 1
	// complexity: O(1)
	private void demote(IAVLNode node) {
		node.setHeight(node.getHeight() - 1);
	}

	// rotates right x-y as we saw at class: y -> upper, x -> lower
	// complexity: O(1)
	private void rightRotate(IAVLNode y, IAVLNode x) {
		if (this.root == y) {
			this.root = x;
		} else {
			if (isLeftChild(y)) {
				y.getParent().setLeft(x);
			} else {
				y.getParent().setRight(x);
			}
		}

		x.setParent(y.getParent());
		y.setParent(x);

		y.setLeft(x.getRight());
		y.getLeft().setParent(y);
		x.setRight(y);

		y.setSize(y.getLeft().getSize() + y.getRight().getSize() + 1);
		x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);
	}

	// rotates left x-y as we saw at class: y -> lower, x -> upper
	// complexity: O(1)
	private void leftRotate(IAVLNode y, IAVLNode x) {
		if (this.root == x) {
			this.root = y;
		} else {
			if (isLeftChild(x)) {
				x.getParent().setLeft(y);
			} else {
				x.getParent().setRight(y);
			}
		}

		y.setParent(x.getParent());
		x.setParent(y);

		x.setRight(y.getLeft());
		x.getRight().setParent(x);
		y.setLeft(x);

		x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);
		y.setSize(y.getLeft().getSize() + y.getRight().getSize() + 1);
	}
	
	// returns the rank-difference between parent and child
	// complexity: O(1)
	private int rankDiff(IAVLNode parent, IAVLNode child) {
		return parent.getHeight() - child.getHeight();
	}
	
	// returns true iff node is a left child
	// complexity: O(1)
	private boolean isLeftChild(IAVLNode node) {
		if (node.getParent() == null) {
			return false;
		}

		return node.getParent().getLeft() == node;
	}

	// balance tree after insert, returns num of balance operations
	// x is left child
	// complexity: O(logn)
	private int insertRebalanceLeft(IAVLNode x) {
		IAVLNode z = x.getParent();
		IAVLNode y = z.getRight();

		IAVLNode a = x.getLeft();
		IAVLNode b = x.getRight();

		// Case 1 - 0,1
		if (rankDiff(z, x) == 0 && rankDiff(z, y) == 1) {
			promote(z);
			return insertRebalance(z) + 1;
		}

		// case 2 - 0,2
		if (rankDiff(z, x) == 0 && rankDiff(z, y) == 2) {
			if (rankDiff(x, a) == 1 && rankDiff(x, b) == 2) { // Case 2
				rightRotate(z, x);
				demote(z);
				return 2;
			}
			if (rankDiff(x, a) == 2 && rankDiff(x, b) == 1) { // Case 3
				leftRotate(b, x);
				rightRotate(z, b);
				demote(x);
				demote(z);
				promote(b);
				return 5;
			}
			// rebalance after join
			if (rankDiff(x, a) == 1 && rankDiff(x, b) == 1) {
				rightRotate(z, x);
				promote(x);
				return insertRebalance(x) + 2;
			}
		}

		return 0;
	}

	// balance tree after insert, returns num of balance operations
	// x is not left child
	// complexity: O(logn)
	private int insertRebalanceRight(IAVLNode x) {
		IAVLNode z = x.getParent();
		IAVLNode y = z.getLeft();

		IAVLNode a = x.getRight();
		IAVLNode b = x.getLeft();

		// Case 1
		if (rankDiff(z, x) == 0 && rankDiff(z, y) == 1) {
			promote(z);
			return insertRebalance(z) + 1;
		}

		// Case "2" 2,0
		if (rankDiff(z, y) == 2 && rankDiff(z, x) == 0) {
			if (rankDiff(x, b) == 2 && rankDiff(x, a) == 1) { // Case 2
				leftRotate(x, z);
				demote(z);
				return 2;
			}
			if (rankDiff(x, b) == 1 && rankDiff(x, a) == 2) { // Case "3" 1,2
				rightRotate(x, b);
				leftRotate(b, z);
				demote(x);
				demote(z);
				promote(b);
				return 5;
			}
			if (rankDiff(x, a) == 1 && rankDiff(x, b) == 1) {
				leftRotate(x, z);
				promote(x);
				return insertRebalance(x) + 2;
			}
		}

		return 0;
	}

	// calls the left or right balance func according to needed
	//returns num of balance operations
	// complexity: O(logn)
	private int insertRebalance(IAVLNode x) {
		if (x.getParent() == null) {
			return 0;
		}

		if (isLeftChild(x)) {
			return insertRebalanceLeft(x);
		} else {
			return insertRebalanceRight(x); // Symmetric
		}
	}
	
	// finds the position to insert a node to the tree, by in-order walk
	// returns null if tree is empty (insert as root), otherwise returns the wanted node
	// pre-cond: node is realNode, node not exists in tree (checked before func call)
	// complexity: O(logn)
	private IAVLNode treePosition(IAVLNode node) {
		if (empty()) {
			return this.root;
		}

		IAVLNode x = this.root;
		IAVLNode y = null;

		while (x.isRealNode()) {
			y = x;
			if (node.getKey() < x.getKey()) {
				x = x.getLeft();
			} else {
				x = x.getRight();
			}
		}

		return y;
	}

	// makes child a child of parent
	// complexity: O(logn)
	private void insertChild(IAVLNode parent, IAVLNode child) {
		if (parent == null) {
			child.setParent(null);
			return;
		}

		if (child.getKey() < parent.getKey()) {
			parent.setLeft(child);
		} else {
			parent.setRight(child);
		}

		child.setParent(parent);
		increaseSize(child);
	}

	// makes the child of child a child of the parent (removes the node child)
	// used when deleting an unary node 
	// complexity: O(1)
	private void replaceChild(IAVLNode parent, IAVLNode child) {
		if (parent == null) {
			child.setParent(null);
			return;
		}

		if (child.getKey() < parent.getKey()) {
			parent.setLeft(child);
		} else {
			parent.setRight(child);
		}

		child.setParent(parent);
	}

	// increases size of all nodes, from node up to root
	// complexity: O(logn)
	private void increaseSize(IAVLNode node) {
		IAVLNode parent = node.getParent();
		while (parent != null) {
			parent.setSize(parent.getSize() + 1);
			parent = parent.getParent();
		}
	}

	// returns true iff node is a leaf
	// complexity: O(1)
	private boolean isLeaf(IAVLNode node) {
		return node.getLeft().isRealNode() == false && node.getRight().isRealNode() == false;
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * Inserts an item with key k and info i to the AVL tree. The tree must remain
	 * valid, i.e. keep its invariants. Returns the number of re-balancing
	 * operations, or 0 if no re-balancing operations were necessary. A
	 * promotion/rotation counts as one re-balance operation, double-rotation is
	 * counted as 2. Returns -1 if an item with key k already exists in the tree.
	 */
	// complexity: O(logn)
	public int insert(int k, String i) {
		IAVLNode node = new AVLNode(k, i);

		// Returns -1 if an item with key k already exists in the tree
		if (search(k) != null) {
			return -1;
		}

		IAVLNode insertPos = treePosition(node);
		if (insertPos == null) {
			this.root = node;
			this.size += 1;
			this.root.setHeight(0);
			return 0;
		}

		// Check for case B: Parent is not a leaf, resulting tree is a valid AVL tree
		if (isLeaf(insertPos) == false) {
			insertChild(insertPos, node);
			this.size++;
			return 0;
		}

		insertChild(insertPos, node);
		this.size++;

		return insertRebalance(node);
	}
	
	

	// replaces node with his successor
	// complexity: O(logn)
	private void replaceWithSuccessor(IAVLNode node) {
		if (node.getRight().isRealNode() == false) {
			return;
		}

		IAVLNode successor = node.getRight();
		while (successor.getLeft().isRealNode()) {
			successor = successor.getLeft();
		}

		int height = successor.getHeight();
		int size = successor.getSize();
		IAVLNode parent = successor.getParent();
		IAVLNode left = successor.getLeft();
		IAVLNode right = successor.getRight();
		boolean successorIsLeftChild = isLeftChild(successor);

		// node and successor are father and son
		if (node.getRight().equals(successor)) {

			successor.setHeight(node.getHeight());
			successor.setSize(node.getSize());

			successor.setRight(node);

			successor.setLeft(node.getLeft());
			successor.getLeft().setParent(successor);

			successor.setParent(node.getParent());
			if (successor.getParent() == null) {
				this.root = successor;
			} else {
				if (isLeftChild(node)) {
					successor.getParent().setLeft(successor);
				} else {
					successor.getParent().setRight(successor);
				}
			}

			node.setHeight(height);
			node.setSize(size);

			node.setParent(successor);

			node.setLeft(left);
			node.getLeft().setParent(node);

			node.setRight(right);
			node.getRight().setParent(node);
		}

		// node and successor are not father & son
		else {
			successor.setHeight(node.getHeight());
			successor.setSize(node.getSize());

			successor.setRight(node.getRight());
			successor.getRight().setParent(successor);

			successor.setLeft(node.getLeft());
			successor.getLeft().setParent(successor);

			successor.setParent(node.getParent());
			if (successor.getParent() == null) {
				this.root = successor;
			} else {
				if (isLeftChild(node)) {
					successor.getParent().setLeft(successor);
				} else {
					successor.getParent().setRight(successor);
				}
			}

			node.setHeight(height);
			node.setSize(size);

			node.setLeft(left);
			node.getLeft().setParent(node);

			node.setRight(right);
			node.getRight().setParent(node);

			node.setParent(parent);
			if (successorIsLeftChild) {
				node.getParent().setLeft(node);
			} else {
				node.getParent().setRight(node);
			}
		}
	}

	// balance tree after delete, returns num of balance operations
	// the left case
	// complexity: O(logn)
	private int deleteRebalanceLeft(IAVLNode z) {
		IAVLNode y = z.getRight();
		IAVLNode a = y.getLeft();
		IAVLNode b = y.getRight();

		// Case 2: 3,1 1,1
		if (rankDiff(y, a) == 1 && rankDiff(y, b) == 1) {
			leftRotate(y, z);
			demote(z);
			promote(y);
			return 3;
		}

		// Case 3: 3,1 2,1
		if (rankDiff(y, a) == 2 && rankDiff(y, b) == 1) {
			leftRotate(y, z);
			demote(z);
			demote(z);
			return deleteRebalance(y.getParent()) + 3;
		}

		// Case 4: 3,1 1,2
		if (rankDiff(y, a) == 1 && rankDiff(y, b) == 2) {
			rightRotate(y, a);
			leftRotate(a, z);
			demote(z);
			demote(z);
			demote(y);
			promote(a);
			return deleteRebalance(a.getParent()) + 6;
		}

		return 0;
	}

	
	// balance tree after insert, returns num of balance operations
	// Right-symmetric to deleteRebalanceLeft
	// complexity: O(logn)
	private int deleteRebalanceRight(IAVLNode z) {
		IAVLNode y = z.getLeft();
		IAVLNode a = y.getLeft();
		IAVLNode b = y.getRight();

		// Case 2: 1,3 1,1
		if (rankDiff(y, a) == 1 && rankDiff(y, b) == 1) {
			rightRotate(z, y);
			demote(z);
			promote(y);
			return 3;
		}

		// Case 3: 1,3 1,2
		if (rankDiff(y, a) == 1 && rankDiff(y, b) == 2) {
			rightRotate(z, y);
			demote(z);
			demote(z);
			return deleteRebalance(y.getParent()) + 3;
		}

		// Case 4: 1,3 2,1
		if (rankDiff(y, a) == 2 && rankDiff(y, b) == 1) {
			leftRotate(b, y);
			rightRotate(z, b);
			demote(z);
			demote(z);
			demote(y);
			promote(b);
			return deleteRebalance(b.getParent()) + 6;
		}

		return 0;
	}

	// calls the right deleteRebalance func, which balances the tree after deletion & returns num of balance op.
	// complexity: O(logn)
	private int deleteRebalance(IAVLNode z) {
		if (z == null) {
			return 0;
		}

		// Case 1: 2,2
		if (rankDiff(z, z.getLeft()) == 2 && rankDiff(z, z.getRight()) == 2) {
			demote(z);
			return deleteRebalance(z.getParent()) + 1;
		}

		// Case 2: 3,1
		if (rankDiff(z, z.getLeft()) == 3 && rankDiff(z, z.getRight()) == 1) {
			return deleteRebalanceLeft(z);
		}

		// Case 2 symmetric: 1,3
		if (rankDiff(z, z.getLeft()) == 1 && rankDiff(z, z.getRight()) == 3) {
			return deleteRebalanceRight(z);
		}

		return 0;
	}

	// delete a leaf-node
	// complexity: O(logn)
	private int deleteLeaf(IAVLNode y) {
		if (this.root == y) {
			this.root = null;
			return 0;
		}

		IAVLNode z = y.getParent();
		if (isLeftChild(y)) {
			z.setLeft(virtualNode);
		} else {
			z.setRight(virtualNode);
		}
		
		decreaseSize(y);

		return deleteRebalance(z);
	}

	// delete an unary-node
	// complexity: O(logn)
	private int deleteUnary(IAVLNode y) {
		IAVLNode z = y.getParent();
		IAVLNode x = null;
		if (y.getLeft().isRealNode() == true) {
			x = y.getLeft();
		} else {
			x = y.getRight();
		}
		if (z != null) {
			replaceChild(z, x);
		} else {
			this.root = x;
		}
		
		decreaseSize(y);
		
		return deleteRebalance(z);
	}

	// decreases size of all nodes, from node up to root
	// complexity: O(logn)
	private void decreaseSize(IAVLNode node) {
		IAVLNode parent = node.getParent();
		while (parent != null) {
			parent.setSize(parent.getSize() - 1);
			parent = parent.getParent();
		}
	}

	/**
	 * public int delete(int k)
	 *
	 * Deletes an item with key k from the binary tree, if it is there. The tree
	 * must remain valid, i.e. keep its invariants. Returns the number of
	 * re-balancing operations, or 0 if no re-balancing operations were necessary. A
	 * promotion/rotation counts as one re-balance operation, double-rotation is
	 * counted as 2. Returns -1 if an item with key k was not found in the tree.
	 */
	// complexity: O(logn)
	public int delete(int k) {
		if (empty()) {
			return -1;
		}

		IAVLNode node = search(this.root, k);

		// Returns -1 if an item with key k was not found in the tree
		if (node == null) {
			return -1;
		}

		// if internal
		if (node.getLeft().isRealNode() && node.getRight().isRealNode()) {
			replaceWithSuccessor(node);
		}

		int res = 0;
		if (isLeaf(node)) {
			res = deleteLeaf(node);
		} else {
			res = deleteUnary(node);
		}

		//decreaseSize(node);
		this.size--;

		return res;
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null if
	 * the tree is empty.
	 */
	// complexity: O(logn)
	public String min() {
		if (empty()) {
			return null;
		}

		IAVLNode node = this.root;
		while (node.getLeft().isRealNode()) {
			node = node.getLeft();
		}

		return node.getValue();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty.
	 */
	// complexity: O(logn)
	public String max() {
		if (empty()) {
			return null;
		}

		IAVLNode node = this.root;
		while (node.getRight().isRealNode()) {
			node = node.getRight();
		}

		return node.getValue();
	}

	// update an array of nodes in the tree, in-order: Helper for keysToArray and infoToArray
	// complexity: O(n)
	public int nodeToArray(IAVLNode node, IAVLNode[] nodes, int index) {
		if (node.isRealNode() == false) {
			return index;
		}
		index = nodeToArray(node.getLeft(), nodes, index);

		nodes[index] = node;
		index++;

		index = nodeToArray(node.getRight(), nodes, index);

		return index;
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 */
	// returns Tree as an array of keys, by order
	// complexity: O(n)
	public int[] keysToArray() {
		IAVLNode[] nodes = new IAVLNode[this.size];

		if (empty()) {
			return new int[this.size];
		}

		nodeToArray(this.root, nodes, 0);

		int[] arrKeys = new int[this.size];
		int i = 0;
		for (IAVLNode node : nodes) {
			arrKeys[i] = node.getKey();
			i++;
		}
		return arrKeys;
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	// returns Tree as an array of info`s, by order (of keys)
	// complexity: O(n)
	public String[] infoToArray() {
		IAVLNode[] nodes = new IAVLNode[this.size];

		if (empty()) {
			return new String[this.size];
		}

		nodeToArray(this.root, nodes, 0);

		String[] arrValues = new String[this.size];
		int i = 0;
		for (IAVLNode node : nodes) {
			arrValues[i] = node.getValue();
			i++;
		}
		return arrValues;
	}
	

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 */
	// complexity: O(1)
	public int size() {
		return this.size;
	}
	
	
	// recursive helper for split, joins sub-trees of the tree to biggerTree/smallerTree, up to the root, as we saw at class
	// complexity: O(logn)
	private void splitRec(IAVLNode x, AVLTree smaller, AVLTree bigger) {
		if (x.getParent() == null) {
			return;
		}

		IAVLNode parent = x.getParent();
		if (isLeftChild(x)) {
			bigger.join(new AVLNode(parent.getKey(), parent.getValue()),
					x.getParent().getRight().isRealNode() ? new AVLTree(x.getParent().getRight()) : new AVLTree());
		} else {
			smaller.join(new AVLNode(parent.getKey(), parent.getValue()),
					x.getParent().getLeft().isRealNode() ? new AVLTree(x.getParent().getLeft()) : new AVLTree());
		}

		splitRec(parent, smaller, bigger);
	}

	/**
	 * public AVLTree[] split(int x)
	 *
	 * splits the tree into 2 trees according to the key x. Returns an array [t1,
	 * t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * 
	 * precondition: search(x) != null (i.e. you can also assume that the tree is
	 * not empty) postcondition: none
	 */
	// complexity: O(logn)

	public AVLTree[] split(int x) {
		AVLTree[] result = new AVLTree[2];
		IAVLNode node = search(this.root, x);

		AVLTree smaller = null;
		if (node.getLeft().isRealNode()) {
			smaller = new AVLTree(node.getLeft());
		} else {
			smaller = new AVLTree();
		}

		AVLTree bigger = null;
		if (node.getRight().isRealNode()) {
			bigger = new AVLTree(node.getRight());
		} else {
			bigger = new AVLTree();
		}

		splitRec(node, smaller, bigger);

		result[0] = smaller;
		result[1] = bigger;
		return result;
	}
	
	

	// fixes nodes size after join
	// complexity: O(|T1.rank - T2.rank| + 1)
	private void fixSize(IAVLNode node) {
		IAVLNode parent = node.getParent();
		while (parent != null) {
			parent.setSize(parent.getLeft().getSize() + parent.getRight().getSize() + 1);
			parent = parent.getParent();
		}
	}

	// joins same size trees (or with rankDiff = 1)
	// complexity: O(1)
	private int joinSameSizeTrees(AVLTree T1, IAVLNode x, AVLTree T2) {
		int rank1 = T1.getRoot().getHeight();
		int rank2 = T2.getRoot().getHeight();
		int res = Math.abs(rank1 - rank2) + 1;
		insertChild(x, T1.getRoot());
		insertChild(x, T2.getRoot());

		x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);
		x.setHeight(Math.max(T1.getRoot().getHeight() + 1, T2.getRoot().getHeight() + 1));
		this.root = x;
		this.size = T1.size + T2.size() + 1;
		return res;
	}

	// joins T1, T2, when T2.rank > T1.rank
	// complexity: O(|T1.rank - T2.rank| + 1)
	private int joinRightTreeIsBigger(AVLTree T1, IAVLNode x, AVLTree T2) {
		int rank1 = T1.getRoot().getHeight();
		int rank2 = T2.getRoot().getHeight();
		int res = Math.abs(rank1 - rank2) + 1;
		
		int k = T1.getRoot().getHeight();

		IAVLNode c = posToJoin(T2.getRoot(), k, true);
		IAVLNode b = c.getLeft();

		x.setHeight(k + 1);

		insertChild(x, T1.getRoot());
		if (b.isRealNode()) {
			insertChild(x, b);
		}
		insertChild(c, x);

		this.root = T2.getRoot();
		this.size = T1.size + T2.size() + 1;

		insertRebalance(x);
		x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);
		fixSize(x);

		return res;
	}

	// joins T1, T2, when T1.rank > T2.rank
	// complexity: O(|T1.rank - T2.rank| + 1)
	private int joinRightTreeIsSmaller(AVLTree T1, IAVLNode x, AVLTree T2) {
		int rank1 = T1.getRoot().getHeight();
		int rank2 = T2.getRoot().getHeight();
		int res = Math.abs(rank1 - rank2) + 1;
		
		int k = T2.getRoot().getHeight();

		IAVLNode c = posToJoin(T1.getRoot(), k, false);
		IAVLNode b = c.getRight();

		x.setHeight(k + 1);

		insertChild(x, T2.getRoot());
		if (b.isRealNode()) {
			insertChild(x, b);
		}
		insertChild(c, x);

		this.root = T1.getRoot();
		this.size = T1.size + T2.size() + 1;

		insertRebalance(x);
		x.setSize(x.getLeft().getSize() + x.getRight().getSize() + 1);
		fixSize(x);

		return res;
	}
	
	// calls the appropriate join func, according the T1`s & T2`s ranks
	// complexity: O(|tree.rank - t.rank| + 1)
	private int joinByOrder(AVLTree T1, IAVLNode x, AVLTree T2) {
		int rankT1 = T1.getRoot().getHeight();
		int rankT2 = T2.getRoot().getHeight();

		if (rankT1 == rankT2 || rankT1 - rankT2 == 1 || rankT2 - rankT1 == 1) {
			int num = joinSameSizeTrees(T1, x, T2);
			return num;
		} else {
			if (rankT1 < rankT2) {
				int num = joinRightTreeIsBigger(T1, x, T2);
				return num;
			} else {
				int num = joinRightTreeIsSmaller(T1, x, T2);
				return num;
			}
		}
	}
	
	// finds the appro. place to join the trees (c from the graph we saw at class)
	// complexity: O(|node.rank - k| + 1) = O(|T1.rank - T2.rank| + 1)
	private IAVLNode posToJoin(IAVLNode node, int k, Boolean isLeft) {
		IAVLNode c = node;

		while (node.getHeight() > k) {
			c = node;
			if (isLeft) {
				node = node.getLeft();
			} else {
				node = node.getRight();
			}
		}

		return c;
	}

	/**
	 * public int join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. Returns the complexity of the operation
	 * (|tree.rank - t.rank| + 1).
	 *
	 * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be
	 * empty (rank = -1). postcondition: none
	 */
	// complexity: O(|tree.rank - t.rank| + 1)

	public int join(IAVLNode x, AVLTree t) {
		if (x.isRealNode() == false) {
			return 0;
		}

		// t or tree are empty
		if (t.empty() && empty()) {
			this.root = x;
			this.size = 1;
			return 1;
		}
		if (t.empty()) {
			insert(x.getKey(), x.getValue());
			return this.getRoot().getHeight() + 1;
		}
		if (empty()) {
			t.insert(x.getKey(), x.getValue());
			this.root = t.getRoot();
			this.size = t.size();
			return t.getRoot().getHeight() + 1;
		}

		int ret = 0;

		if (t.getRoot().getKey() < this.root.getKey()) {
			ret = joinByOrder(t, x, this);
		} else {
			ret = joinByOrder(this, x, t);
		}

		return ret;
	}

	

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 */
	// complexity: O(1)
	public IAVLNode getRoot() {
		return this.root;
	}
	
	

	/**
	 * public interface IAVLNode ! Do not delete or modify this - otherwise all
	 * tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); // Returns node's key (for virtual node return -1).

		public String getValue(); // Returns node's value [info], for virtual node returns null.

		public void setLeft(IAVLNode node); // Sets left child.

		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.

		public void setRight(IAVLNode node); // Sets right child.

		public IAVLNode getRight(); // Returns right child, if there is no right child return null.

		public void setParent(IAVLNode node); // Sets parent.

		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.

		public void setHeight(int height); // Sets the height of the node.

		public int getHeight(); // Returns the height of the node (-1 for virtual nodes).

		public int getSize(); // Returns the size of the nodes-subTree (including him)

		public void setSize(int newSize); // Sets the size of the node
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree (for example AVLNode), do
	 * it in this file, not in another file.
	 * 
	 * This class can and MUST be modified (It must implement IAVLNode).
	 */
	public class AVLNode implements IAVLNode {

		// AVLNode fields
		protected String info;
		protected int key;
		protected IAVLNode left;
		protected IAVLNode right;
		protected IAVLNode parent;
		protected int rank;
		protected int size;
		protected boolean realNode;

		// AVLNode constructors
		public AVLNode(int key, String info) {
			this.key = key;
			this.info = info;
			this.left = virtualNode;
			this.right = virtualNode;
			this.parent = null;
			this.rank = 0;
			this.size = 1;
			this.realNode = true;
		}

		public AVLNode(int key) {
			this(key, null);
		}

		// returns the node`s key
		// complexity: O(1)
		@Override
		public int getKey() {
			return this.key;
		}

		// returns the node`s info
		// complexity: O(1)
		@Override
		public String getValue() {
			return this.info;
		}

		// set nodes left son
		// complexity: O(1)
		@Override
		public void setLeft(IAVLNode node) {
			this.left = node;

		}

		// returns the node`s left child
		// complexity: O(1)
		@Override
		public IAVLNode getLeft() {
			return this.left;
		}

		// set nodes right son
		// complexity: O(1)
		@Override
		public void setRight(IAVLNode node) {
			this.right = node;
		}

		// returns the node`s right child
		// complexity: O(1)
		@Override
		public IAVLNode getRight() {
			return this.right;
		}

		// sets the node`s parent
		// complexity: O(1)
		@Override
		public void setParent(IAVLNode node) {
			this.parent = node;
		}

		// returns the node`s parent
		// complexity: O(1)
		@Override
		public IAVLNode getParent() {
			return this.parent;
		}

		// returns true iff node is not a virtual-node
		// complexity: O(1)
		@Override
		public boolean isRealNode() {
			return this.realNode;
		}

		// sets the node`s height/ rank
		// complexity: O(1)
		@Override
		public void setHeight(int height) {
			this.rank = height;
		}

		// returns the node`s height/ rank
		// complexity: O(1)
		@Override
		public int getHeight() {
			return this.rank;
		}

		// returns the node`s size
		// complexity: O(1)
		@Override
		public int getSize() {
			return this.size;
		}

		// sete the node`s size
		// complexity: O(1)
		@Override
		public void setSize(int newSize) {
			this.size = newSize;
		}

	}

	public static final class VirtualNode implements IAVLNode {

		// sets the virtual-node, constant fields, one instance overall (re-use)
		// no option of changing it`s fields
		
		@Override
		public int getKey() {
			return -1;
		}

		@Override
		public String getValue() {
			return null;
		}

		@Override
		public void setHeight(int height) {
			// Do nothing.
		}

		@Override
		public void setLeft(IAVLNode node) {
			// Do nothing.
		}

		@Override
		public IAVLNode getLeft() {
			return null;
		}

		@Override
		public void setRight(IAVLNode node) {
			// Do nothing.
		}

		@Override
		public IAVLNode getRight() {
			return null;
		}

		@Override
		public void setParent(IAVLNode node) {
			// Do nothing.
		}

		@Override
		public IAVLNode getParent() {
			return null;
		}

		@Override
		public boolean isRealNode() {
			return false;
		}

		@Override
		public int getHeight() {
			return -1;
		}

		@Override
		public int getSize() {
			return 0;
		}

		@Override
		public void setSize(int newSize) {
			// Do nothing.
		}
	}

}
