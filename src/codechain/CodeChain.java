package codechain;

public final class CodeChain<InputType,OutputType> {
	private CodeBlock<InputType,?,CodeBlock,OutputType> head;
	
	private CodeChain(CodeBlock head){
		this.head = head;
	}
	
	/**
	 * Returns a new CodeChain starting with the inputted CodeBlock
	 * @param head a CodeBlock with the same input type and output type as the CodeChain
	 */
	public static <Input,Output> CodeChain<Input,Output> build(CodeBlock<Input,Output,CodeBlock,Output> head){
		if(! head.hasNext())
			return new CodeChain<Input,Output>(head);
		else
			throw new IllegalArgumentException("The inputted CodeBlock has a next value other than null");
	}
	
	/**
	 * Returns a new empty CodeChain
	 */
	public static <Input,Output> CodeChain<Input,Output> build(){
		return new CodeChain<>(null);
	}
	
	
	/**
	 * Create a new CodeChain with the inputted CodeBlock added to the end of the current CodeChain
	 * @param block a CodeBlock that takes as input the current CodeChain's output
	 * @return CodeChain with block added to the end
	 */
	public <NewOutput> CodeChain<InputType,NewOutput> add(
			CodeBlock<OutputType,NewOutput,CodeBlock,NewOutput> block){	
		if(! block.hasNext())
			return this.addMultiple(block);
		else
			throw new IllegalArgumentException("The inputted CodeBlock has a next value other than null");
	}
	
	/**
	 * Create a new CodeChain with the inputted CodeChain added to the end of the current CodeChain
	 * @param chain a CodeChain that takes as input the current CodeChain's output
	 * @return CodeChain with chain added to the end
	 */
	public <NewOutput> CodeChain<InputType,NewOutput> add(
			CodeChain<OutputType, NewOutput> chain){
		return this.addMultiple(chain.head);
	}
	
	private <NewOutput> CodeChain<InputType,NewOutput> addMultiple(
			CodeBlock<OutputType,?,CodeBlock,NewOutput> block){	
		//adds block to the end of an existing chain or sets it as the beginning of a previously empty chain
		if(head != null)
			return new CodeChain<InputType,NewOutput> (head.addToEnd(block));
		else{
			return new CodeChain<InputType,NewOutput> (block);
		}
	}
	
	/**
	 * Runs the code stored in each block on the CodeChain and returns the result
	 * @param input the input of the first CodeBlock
	 * @return the output of the last CodeBlock
	 */
	public OutputType run(InputType input){
		return head.run(input);
	}
}
