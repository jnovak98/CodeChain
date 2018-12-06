package codechain;

public class CodeChain<I extends ValueNode,O extends ValueNode> {
	private CodeBlock<I,ValueNode,CodeBlock,O> head;
	
	public CodeChain(CodeBlock head){
		this.head = head;
	}
	
	public <NewOutput extends ValueNode,Next extends CodeBlock> CodeChain<I,NewOutput> add(
			CodeBlock<O,NewOutput,Next,NewOutput> block){
		return new CodeChain<I,NewOutput> (head.addToEnd(block));
	}
	
	public O run(I input){
		return head.run(input);
	}
}
