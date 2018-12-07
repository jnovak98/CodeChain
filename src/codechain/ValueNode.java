package codechain;

public final class ValueNode <NodeType,NextNodeType extends ValueNode> {
	private final NodeType value;
	private NextNodeType next;
	
	/**
	 * Returns a new single ValueNode storing value
	 * @param value the value to be stored
	 */
	public ValueNode(NodeType value){
		this.value = value;
		this.next = null;
	}
	
	/**
	 * Returns a new ValueNode storing value and holding other ValueNode next
	 * @param value the value to be stored
	 * @param next the next nested ValueNode 
	 */
	public ValueNode(NodeType value, NextNodeType next) {
		this.value = value;
		this.next = next;
	}
	
	/**
	 * @return the next nested ValueNode
	 */
	public NextNodeType next() {
		return next;
	}
	
	/**
	 * @return a boolean representing if there is another ValueNode nested in the current ValueNode
	 */
	public boolean hasNext(){
		return (next != null);
	}

	public <X,Y extends ValueNode> ValueNode addToEnd(ValueNode<X,Y> node){
		if(hasNext())
			return new ValueNode<NodeType,ValueNode>(value,next.addToEnd(node));
		else
			return new ValueNode<NodeType,ValueNode<X,Y>>(value,node);			
	}
	
	/**
	 * @return the value stored in ValueNode
	 */
	public final NodeType getValue() {
		return value;
	}

	
}
