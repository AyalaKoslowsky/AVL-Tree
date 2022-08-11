
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
