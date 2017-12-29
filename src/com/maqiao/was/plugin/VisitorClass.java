package com.maqiao.was.plugin;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class VisitorClass extends ASTVisitor{

    @Override  
    public boolean visit(FieldDeclaration node) {  
        for (Object obj: node.fragments()) {  
            VariableDeclarationFragment v = (VariableDeclarationFragment)obj;  
            System.out.println("Field:\t" + v.getName());  
        }  
          
        return true;  
    }  
  
	@Override  
	@SuppressWarnings("unchecked")
    public boolean visit(MethodDeclaration node) {  
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++"); 
        System.out.println("Method:\t" + node.getName()); 
		List<Statement>list=node.getBody().statements();
    	for(int i=0;i<list.size();i++) {
            System.out.println("---------------------------------------"); 
    		Statement e=list.get(i);
    		System.out.print("Statement e:"+e.toString());
    		System.out.println("Statement e:"+e.getStartPosition());
    		//System.out.println("Statement e:"+e.getLeadingComment());
    		//System.out.println("Statement e:"+e.getLocationInParent().getId());
    		//System.out.println("Statement e:"+e.structuralPropertiesForType());
    		//List<Object> list2=e.structuralPropertiesForType();
    		//for(int ii=0;ii<list2.size();ii++)
    			//System.out.println("List["+ii+"]:"+list2.get(ii).toString());
    		//ASTNode astnode=e.getRoot();
    		//ConditionalExpression f=Utils.getConditionalExpression(e.toString().toCharArray());
    		//AST ast=f.getAST();
    		
    	}
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++"); 
        return true;  
    }  
  
    @Override  
    public boolean visit(TypeDeclaration node) {  
        System.out.println("Class:\t" + node.getName());  
        return true;  
    }  
}
