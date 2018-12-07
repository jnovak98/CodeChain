package codechain;

import java.util.function.Function;

public class CodeBlock<InputType,OutputType,NextBlockType extends CodeBlock, ChainOutput> {
	
	private final Function<InputType,OutputType> code;
	private NextBlockType next;
	private String name;
	
	/**
	 * Returns a new unnamed CodeBlock holding the inputted Function
	 * @param code a Function matching the CodeBlock in input and output
	 */
	public CodeBlock(Function<InputType, OutputType> code) {
		this(null,code,null);
	}
	
	/**
	 * Returns a new named CodeBlock holding the inputted Function]
	 * @param name the name a function can be called by
	 * @param code a Function matching the CodeBlock in input and output
	 */
	public CodeBlock(String name, Function<InputType, OutputType> code) {
		this(name,code,null);
	}
	
	private CodeBlock(String name, Function<InputType, OutputType> code, NextBlockType next) {
		this.name = name;
		this.code = code;
		this.next = next;
	}
	
	/**
	 * Runs the Function stored in this CodeBlock on input
	 * @param input the input to the CodeBlock's Function
	 * @return the Function's output for the given input
	 */
	public ChainOutput run(InputType input){
		//recursively applies each function in the chain
		if(next == null)
			return (ChainOutput) code.apply(input);
		else
			return (ChainOutput) next.run(code.apply(input));
	}
	
	/**
	 * Runs the Function stored in this CodeBlock on input, iff the inputted name matches that stored in the CodeBlock
	 * @param name the name of the Function to be called, set by the constructor
	 * @param input the input to the CodeBlock's 
	 * @return the Function's output for the given input
	 */
	public ChainOutput run(String name, InputType input){
		if(name.equals(this.name))
			return run(input);
		else
			throw new IllegalArgumentException("Cannot call function " + name + " on CodeBlock " + this.name);
	}
	
	<BlockOutputType,BlockNextType extends CodeBlock,NewChainOutput> CodeBlock addToEnd(CodeBlock<ChainOutput,BlockOutputType,BlockNextType,NewChainOutput> block){
		if(next == null)
			return new CodeBlock<InputType,OutputType,CodeBlock<ChainOutput,BlockOutputType,BlockNextType,NewChainOutput>,NewChainOutput>(name,code,block);		
		else
			return new CodeBlock<InputType,OutputType,CodeBlock,NewChainOutput>(name, code,next.addToEnd(block));	
	}
	
	boolean hasNext() {
		return (next != null);
	}
}
