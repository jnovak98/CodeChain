package codechain;

public class ValueNode <T,U extends ValueNode> {
	private final T value;
	private U next;
	
	public ValueNode(T value){
		this.value = value;
		this.next = null;
	}
	
	public ValueNode(T value, U next) {
		this.value = value;
		this.next = next;
	}

	public U next() {
		return next;
	}
	
	public boolean hasNext(){
		return (next != null);
	}

	public <X,Y extends ValueNode> ValueNode addToEnd(ValueNode<X,Y> node){
		if(hasNext())
			return new ValueNode<T,ValueNode>(value,next.addToEnd(node));
		else
			return new ValueNode<T,ValueNode<X,Y>>(value,node);			
	}
	
	/**
	 * @return the value
	 */
	public final T getValue() {
		return value;
	}

	
}
