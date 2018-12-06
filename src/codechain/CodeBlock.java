package codechain;

import java.util.function.Function;

public class CodeBlock<InputType extends ValueNode,OutputType extends ValueNode,NextBlockType extends CodeBlock,
	ChainOutput extends ValueNode> {
	
	private final Function<InputType,OutputType> code;
	private NextBlockType next;
	private String name;
	
	public CodeBlock(Function<InputType, OutputType> code) {
		this(null,code,null);
	}
	
	public CodeBlock(Function<InputType, OutputType> code, NextBlockType next) {
		this(null,code,next);
	}
	
	public CodeBlock(String name, Function<InputType, OutputType> code, NextBlockType next) {
		this.name = name;
		this.code = code;
		this.next = next;
	}
	
	public NextBlockType next() {
		return next;
	}
	
	public boolean hasNext(){
		return (next!=null);
	}

	public ChainOutput run(InputType input){
		if(next == null)
			return (ChainOutput) code.apply(input);
		else
			return (ChainOutput) next.run(code.apply(input));
	}
	
	public ChainOutput run(String name, InputType input){
		if(name.equals(this.name))
			return run(input);
		else
			throw new IllegalArgumentException("Cannot call function " + name + " on CodeBlock " + this.name);
	}
	
	public <X extends ValueNode,Y extends ValueNode,Z extends CodeBlock,O extends ValueNode> CodeBlock addToEnd(CodeBlock<X,Y,Z,O> block){
		if(hasNext())
			return new CodeBlock<InputType,OutputType,CodeBlock,O>(name, code,next.addToEnd(block));
		else
			return new CodeBlock<InputType,OutputType,CodeBlock<X,Y,Z,O>,O>(name,code,block);		
	}
}
